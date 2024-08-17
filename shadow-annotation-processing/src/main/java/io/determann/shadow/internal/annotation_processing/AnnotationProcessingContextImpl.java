package io.determann.shadow.internal.annotation_processing;

import io.determann.shadow.api.annotation_processing.AnnotationProcessingContext;
import io.determann.shadow.api.annotation_processing.DiagnosticContext;
import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelConstants;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.lang_model.LangModelContextImplementation;
import io.determann.shadow.api.lang_model.shadow.AnnotationableLangModel;
import io.determann.shadow.api.lang_model.shadow.structure.*;
import io.determann.shadow.api.lang_model.shadow.type.*;
import io.determann.shadow.api.shadow.Annotationable;
import io.determann.shadow.api.shadow.QualifiedNameable;
import io.determann.shadow.api.shadow.structure.Field;
import io.determann.shadow.api.shadow.structure.Module;
import io.determann.shadow.api.shadow.structure.Parameter;
import io.determann.shadow.api.shadow.structure.RecordComponent;
import io.determann.shadow.api.shadow.type.Class;
import io.determann.shadow.api.shadow.type.Record;
import io.determann.shadow.api.shadow.type.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.*;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

import static io.determann.shadow.api.lang_model.LangModelAdapter.generalize;
import static io.determann.shadow.api.lang_model.LangModelAdapter.particularElement;
import static io.determann.shadow.api.shadow.Operations.QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;
import static java.lang.System.err;
import static java.lang.System.out;
import static java.util.stream.Collectors.toSet;

public class AnnotationProcessingContextImpl implements AnnotationProcessingContext,
                                                        LangModelContextImplementation
{
   private final ProcessingEnvironment processingEnv;
   private final RoundEnvironment roundEnv;
   private final int processingRound;
   private final LangModelContext langModelContext;
   private BiConsumer<AnnotationProcessingContext, Throwable> exceptionHandler = (shadowApi, throwable) ->
   {
      StringWriter stringWriter = new StringWriter();
      PrintWriter printWriter = new PrintWriter(stringWriter);
      throwable.printStackTrace(printWriter);
      logAndRaiseError(stringWriter.toString());
      throw new RuntimeException(throwable);
   };
   private BiConsumer<AnnotationProcessingContext, DiagnosticContext> diagnosticHandler = (shadowApi, diagnosticContext) ->
   {
      if (!shadowApi.isProcessingOver())
      {
         String duration = Duration.between(diagnosticContext.getStart(), diagnosticContext.getEnd()).toString()
                                   .substring(2)
                                   .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                                   .toLowerCase();

         logInfo(diagnosticContext.getProcessorName() +
                 " took " +
                 duration +
                 " in round " +
                 diagnosticContext.getProcessingRound() +
                 "\n");
      }
   };
   private BiConsumer<AnnotationProcessingContext, String> systemOutHandler = (shadowApi, s) ->
   {
      if (!getProcessingEnv().toString().startsWith("javac"))
      {
         logWarning(s);
      }
   };
   private BiConsumer<AnnotationProcessingContext, String> systemErrorHandler = AnnotationProcessingContext::logAndRaiseError;


   public AnnotationProcessingContextImpl(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv, int processingRound)
   {
      this.langModelContext = generalize(processingEnv.getTypeUtils(), processingEnv.getElementUtils());
      this.processingRound = processingRound;
      this.processingEnv = processingEnv;
      this.roundEnv = roundEnv;

      proxySystemOut();
      proxySystemErr();
   }

   private void proxySystemOut()
   {
      //in >= java 18 out.getCharset()
      PrintStream printStream = new PrintStream(out)
      {
         @Override
         public void println(String x)
         {
            super.println(x);
            if (x != null && systemOutHandler != null)
            {
               systemOutHandler.accept(AnnotationProcessingContextImpl.this, x);
            }
         }
      };
      System.setOut(printStream);
   }

   private void proxySystemErr()
   {
      //in >= java 18 out.getCharset()
      PrintStream printStream = new PrintStream(err)
      {
         @Override
         public void println(String x)
         {
            super.println(x);
            if (x != null && systemErrorHandler != null)
            {
               systemErrorHandler.accept(AnnotationProcessingContextImpl.this, x);
            }
         }
      };
      System.setErr(printStream);
   }

   private <TYPE> Set<TYPE> getAnnotated(String qualifiedAnnotation, java.lang.Class<TYPE> typeClass)
   {
      TypeElement annotation = getProcessingEnv().getElementUtils().getTypeElement(qualifiedAnnotation);
      if (annotation == null || !annotation.getKind().equals(ElementKind.ANNOTATION_TYPE))
      {
         throw new IllegalArgumentException("No annotation found with qualified name \"" + qualifiedAnnotation + "\"");
      }
      return getAnnotated(annotation, typeClass);
   }

   private <TYPE> Set<TYPE> getAnnotated(TypeElement annotation, java.lang.Class<TYPE> typeClass)
   {
      return getRoundEnv().getElementsAnnotatedWith(annotation).stream()
                          .map(element ->
                               {
                                  if (element.getKind().isExecutable())
                                  {
                                     return (Annotationable) generalize(getApi(), ((ExecutableElement) element));
                                  }
                                  return ((Annotationable) generalize(getApi(), element));
                               })
                          .filter(typeClass::isInstance)
                          .map(typeClass::cast)
                          .collect(toSet());
   }

   private <RESULT> Set<RESULT> getAnnotated(QualifiedNameable input, java.lang.Class<RESULT> resultClass)
   {
      if (input instanceof AnnotationLangModel annotationLangModel)
      {
         return getAnnotated(particularElement(annotationLangModel), resultClass);
      }
      return getAnnotated(requestOrThrow(input, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME), resultClass);
   }

   @Override
   public Set<AnnotationableLangModel> getAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, AnnotationableLangModel.class);
   }

   @Override
   public Set<AnnotationableLangModel> getAnnotatedWith(Annotation annotation)
   {
      return getAnnotated(annotation, AnnotationableLangModel.class);
   }

   @Override
   public Set<DeclaredLangModel> getDeclaredAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, DeclaredLangModel.class);
   }

   @Override
   public Set<DeclaredLangModel> getDeclaredAnnotatedWith(Annotation annotation)
   {
      return getAnnotated(annotation, DeclaredLangModel.class);
   }

   @Override
   public Set<ClassLangModel> getClassesAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, ClassLangModel.class);
   }

   @Override
   public Set<ClassLangModel> getClassesAnnotatedWith(Annotation annotation)
   {
      return getAnnotated(annotation, ClassLangModel.class);
   }

   @Override
   public Set<EnumLangModel> getEnumsAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, EnumLangModel.class);
   }

   @Override
   public Set<EnumLangModel> getEnumsAnnotatedWith(Annotation annotation)
   {
      return getAnnotated(annotation, EnumLangModel.class);
   }

   @Override
   public Set<InterfaceLangModel> getInterfacesAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, InterfaceLangModel.class);
   }

   @Override
   public Set<InterfaceLangModel> getInterfacesAnnotatedWith(Annotation annotation)
   {
      return getAnnotated(annotation, InterfaceLangModel.class);
   }

   @Override
   public Set<RecordLangModel> getRecordsAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, RecordLangModel.class);
   }

   @Override
   public Set<RecordLangModel> getRecordsAnnotatedWith(Annotation annotation)
   {
      return getAnnotated(annotation, RecordLangModel.class);
   }

   @Override
   public Set<FieldLangModel> getFieldsAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, FieldLangModel.class);
   }

   @Override
   public Set<FieldLangModel> getFieldsAnnotatedWith(Annotation annotation)
   {
      return getAnnotated(annotation, FieldLangModel.class);
   }

   @Override
   public Set<ParameterLangModel> getParametersAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, ParameterLangModel.class);
   }

   @Override
   public Set<ParameterLangModel> getParametersAnnotatedWith(Annotation annotation)
   {
      return getAnnotated(annotation, ParameterLangModel.class);
   }

   @Override
   public Set<MethodLangModel> getMethodsAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, MethodLangModel.class);
   }

   @Override
   public Set<MethodLangModel> getMethodsAnnotatedWith(Annotation annotation)
   {
      return getAnnotated(annotation, MethodLangModel.class);
   }

   @Override
   public Set<ConstructorLangModel> getConstructorsAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, ConstructorLangModel.class);
   }

   @Override
   public Set<ConstructorLangModel> getConstructorsAnnotatedWith(Annotation annotation)
   {
      return getAnnotated(annotation, ConstructorLangModel.class);
   }

   @Override
   public Set<AnnotationLangModel> getAnnotationsAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, AnnotationLangModel.class);
   }

   @Override
   public Set<AnnotationLangModel> getAnnotationsAnnotatedWith(Annotation annotation)
   {
      return getAnnotated(annotation, AnnotationLangModel.class);
   }

   @Override
   public Set<PackageLangModel> getPackagesAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, PackageLangModel.class);
   }

   @Override
   public Set<PackageLangModel> gePackagesAnnotatedWith(Annotation annotation)
   {
      return getAnnotated(annotation, PackageLangModel.class);
   }

   @Override
   public Set<GenericLangModel> getGenericsAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, GenericLangModel.class);
   }

   @Override
   public Set<GenericLangModel> geGenericsAnnotatedWith(Annotation annotation)
   {
      return getAnnotated(annotation, GenericLangModel.class);
   }

   @Override
   public Set<ModuleLangModel> getModulesAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, ModuleLangModel.class);
   }

   @Override
   public Set<ModuleLangModel> geModulesAnnotatedWith(Annotation annotation)
   {
      return getAnnotated(annotation, ModuleLangModel.class);
   }

   @Override
   public Set<RecordComponentLangModel> getRecordComponentsAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, RecordComponentLangModel.class);
   }

   @Override
   public Set<RecordComponentLangModel> geRecordComponentsAnnotatedWith(Annotation annotation)
   {
      return getAnnotated(annotation, RecordComponentLangModel.class);
   }

   @Override
   public void writeAndCompileSourceFile(String qualifiedName, String content)
   {
      try (Writer writer = getProcessingEnv().getFiler().createSourceFile(qualifiedName).openWriter())
      {
         writer.write(content);
      }
      catch (IOException e)
      {
         throw new RuntimeException(e);
      }
   }

   @Override
   public void writeClassFile(String qualifiedName, String content)
   {
      try (Writer writer = getProcessingEnv().getFiler().createClassFile(qualifiedName).openWriter())
      {
         writer.write(content);
      }
      catch (IOException e)
      {
         throw new RuntimeException(e);
      }
   }

   @Override
   public void writeResource(StandardLocation location, String moduleAndPkg, String relativPath, String content)
   {
      try (Writer writer = getProcessingEnv().getFiler().createResource(location, moduleAndPkg, relativPath).openWriter())
      {
         writer.write(content);
      }
      catch (IOException e)
      {
         throw new RuntimeException(e);
      }
   }

   @Override
   public FileObject readResource(StandardLocation location, String moduleAndPkg, String relativPath) throws IOException
   {
      return getProcessingEnv().getFiler().getResource(location, moduleAndPkg, relativPath);
   }

   @Override
   public boolean isProcessingOver()
   {
      return getRoundEnv().processingOver();
   }

   @Override
   public boolean isFirstRound()
   {
      return processingRound == 0;
   }

   @Override
   public int getProcessingRound()
   {
      return processingRound;
   }

   @Override
   public void setExceptionHandler(BiConsumer<AnnotationProcessingContext, Throwable> exceptionHandler)
   {
      this.exceptionHandler = exceptionHandler;
   }

   @Override
   public BiConsumer<AnnotationProcessingContext, Throwable> getExceptionHandler()
   {
      return exceptionHandler;
   }

   @Override
   public void setDiagnosticHandler(BiConsumer<AnnotationProcessingContext, DiagnosticContext> diagnosticHandler)
   {
      this.diagnosticHandler = diagnosticHandler;
   }

   @Override
   public BiConsumer<AnnotationProcessingContext, DiagnosticContext> getDiagnosticHandler()
   {
      return diagnosticHandler;
   }

   @Override
   public void setSystemOutHandler(BiConsumer<AnnotationProcessingContext, String> systemOutHandler)
   {
      this.systemOutHandler = systemOutHandler;
   }

   @Override
   public BiConsumer<AnnotationProcessingContext, String> getSystemOutHandler()
   {
      return systemOutHandler;
   }

   @Override
   public void setSystemErrorHandler(BiConsumer<AnnotationProcessingContext, String> systemErrorHandler)
   {
      this.systemErrorHandler = systemErrorHandler;
   }

   @Override
   public BiConsumer<AnnotationProcessingContext, String> getSystemErrorHandler()
   {
      return systemErrorHandler;
   }

   @Override
   public void logAndRaiseError(String msg)
   {
      processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg);
   }

   @Override
   public void logInfo(String msg)
   {
      processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, msg);
   }

   @Override
   public void logWarning(String msg)
   {
      processingEnv.getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING, msg);
   }

   @Override
   public void logAndRaiseErrorAt(AnnotationableLangModel annotationable, String msg)
   {
      processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg, particularElement(annotationable));
   }

   @Override
   public void logInfoAt(AnnotationableLangModel annotationable, String msg)
   {
      processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, msg, particularElement(annotationable));
   }

   @Override
   public void logWarningAt(AnnotationableLangModel annotationable, String msg)
   {
      processingEnv.getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING, msg, particularElement(annotationable));
   }

   public AnnotationProcessingContext getApi()
   {
      return this;
   }

   @Override
   public List<ModuleLangModel> getModules()
   {
      return langModelContext.getModules();
   }

   @Override
   public Optional<ModuleLangModel> getModule(String name)
   {
      return langModelContext.getModule(name);
   }

   @Override
   public ModuleLangModel getModuleOrThrow(String name)
   {
      return langModelContext.getModuleOrThrow(name);
   }

   @Override
   public List<PackageLangModel> getPackages(String qualifiedName)
   {
      return langModelContext.getPackages(qualifiedName);
   }

   @Override
   public List<PackageLangModel> getPackages()
   {
      return langModelContext.getPackages();
   }

   @Override
   public Optional<PackageLangModel> getPackage(String qualifiedModuleName, String qualifiedPackageName)
   {
      return langModelContext.getPackage(qualifiedModuleName, qualifiedPackageName);
   }

   @Override
   public PackageLangModel getPackageOrThrow(String qualifiedModuleName, String qualifiedPackageName)
   {
      return langModelContext.getPackageOrThrow(qualifiedModuleName, qualifiedPackageName);
   }

   @Override
   public Optional<PackageLangModel> getPackage(Module module, String qualifiedPackageName)
   {
      return langModelContext.getPackage(module, qualifiedPackageName);
   }

   @Override
   public PackageLangModel getPackageOrThrow(Module module, String qualifiedPackageName)
   {
      return langModelContext.getPackageOrThrow(module, qualifiedPackageName);
   }

   @Override
   public LangModelConstants getConstants()
   {
      return langModelContext.getConstants();
   }

   @Override
   public ClassLangModel withGenerics(Class aClass, Shadow... generics)
   {
      return langModelContext.withGenerics(aClass, generics);
   }

   @Override
   public InterfaceLangModel withGenerics(Interface anInterface, Shadow... generics)
   {
      return langModelContext.withGenerics(anInterface, generics);
   }

   @Override
   public RecordLangModel withGenerics(Record aRecord, Shadow... generics)
   {
      return langModelContext.withGenerics(aRecord, generics);
   }

   @Override
   public ClassLangModel erasure(Class aClass)
   {
      return langModelContext.erasure(aClass);
   }

   @Override
   public InterfaceLangModel erasure(Interface anInterface)
   {
      return langModelContext.erasure(anInterface);
   }

   @Override
   public RecordLangModel erasure(Record aRecord)
   {
      return langModelContext.erasure(aRecord);
   }

   @Override
   public ArrayLangModel erasure(Array array)
   {
      return langModelContext.erasure(array);
   }

   @Override
   public ShadowLangModel erasure(Wildcard wildcard)
   {
      return langModelContext.erasure(wildcard);
   }

   @Override
   public ShadowLangModel erasure(Generic generic)
   {
      return langModelContext.erasure(generic);
   }

   @Override
   public ShadowLangModel erasure(Intersection intersection)
   {
      return langModelContext.erasure(intersection);
   }

   @Override
   public RecordComponentLangModel erasure(RecordComponent recordComponent)
   {
      return langModelContext.erasure(recordComponent);
   }

   @Override
   public ShadowLangModel erasure(Parameter parameter)
   {
      return langModelContext.erasure(parameter);
   }

   @Override
   public ShadowLangModel erasure(Field field)
   {
      return langModelContext.erasure(field);
   }

   @Override
   public ClassLangModel interpolateGenerics(Class aClass)
   {
      return langModelContext.interpolateGenerics(aClass);
   }

   @Override
   public InterfaceLangModel interpolateGenerics(Interface anInterface)
   {
      return langModelContext.interpolateGenerics(anInterface);
   }

   @Override
   public RecordLangModel interpolateGenerics(Record aRecord)
   {
      return langModelContext.interpolateGenerics(aRecord);
   }

   @Override
   public ArrayLangModel asArray(Array array)
   {
      return langModelContext.asArray(array);
   }

   @Override
   public ArrayLangModel asArray(Primitive primitive)
   {
      return langModelContext.asArray(primitive);
   }

   @Override
   public ArrayLangModel asArray(Declared declared)
   {
      return langModelContext.asArray(declared);
   }

   @Override
   public ArrayLangModel asArray(Intersection intersection)
   {
      return langModelContext.asArray(intersection);
   }

   @Override
   public WildcardLangModel asExtendsWildcard(Array array)
   {
      return langModelContext.asExtendsWildcard(array);
   }

   @Override
   public WildcardLangModel asSuperWildcard(Array array)
   {
      return langModelContext.asSuperWildcard(array);
   }

   @Override
   public WildcardLangModel asExtendsWildcard(Declared array)
   {
      return langModelContext.asExtendsWildcard(array);
   }

   @Override
   public WildcardLangModel asSuperWildcard(Declared array)
   {
      return langModelContext.asSuperWildcard(array);
   }

   @Override
   public List<DeclaredLangModel> getDeclared()
   {
      return langModelContext.getDeclared();
   }

   @Override
   public Optional<DeclaredLangModel> getDeclared(String qualifiedName)
   {
      return langModelContext.getDeclared(qualifiedName);
   }

   @Override
   public Types getTypes()
   {
      return LangModelAdapter.getTypes(langModelContext);
   }

   @Override
   public Elements getElements()
   {
      return LangModelAdapter.getElements(langModelContext);
   }

   public ProcessingEnvironment getProcessingEnv()
   {
      return processingEnv;
   }

   public RoundEnvironment getRoundEnv()
   {
      return roundEnv;
   }
}

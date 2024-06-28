package io.determann.shadow.internal.annotation_processing;

import io.determann.shadow.api.annotation_processing.AnnotationProcessingContext;
import io.determann.shadow.api.annotation_processing.DiagnosticContext;
import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelConstants;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.lang_model.LangModelContextImplementation;
import io.determann.shadow.api.shadow.Annotationable;
import io.determann.shadow.api.shadow.structure.Module;
import io.determann.shadow.api.shadow.structure.Package;
import io.determann.shadow.api.shadow.structure.*;
import io.determann.shadow.api.shadow.type.Class;
import io.determann.shadow.api.shadow.type.Enum;
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
import java.util.function.Function;

import static io.determann.shadow.api.converter.Converter.convert;
import static io.determann.shadow.api.lang_model.LangModelAdapter.generalize;
import static io.determann.shadow.api.lang_model.LangModelAdapter.particularElement;
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

   private <TYPE> Set<TYPE> getAnnotated(String qualifiedAnnotation, Function<Annotationable, Optional<TYPE>> converter)
   {
      TypeElement annotation = getProcessingEnv().getElementUtils().getTypeElement(qualifiedAnnotation);
      if (annotation == null || !annotation.getKind().equals(ElementKind.ANNOTATION_TYPE))
      {
         throw new IllegalArgumentException("No annotation found with qualified name \"" + qualifiedAnnotation + "\"");
      }
      return getAnnotated(annotation, converter);
   }

   private <TYPE> Set<TYPE> getAnnotated(TypeElement annotation, Function<Annotationable, Optional<TYPE>> converter)
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
                          .map(converter)
                          .filter(Optional::isPresent)
                          .map(Optional::get)
                          .collect(toSet());
   }

   @Override
   public Set<Annotationable> getAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, Optional::of);
   }

   @Override
   public Set<Annotationable> getAnnotatedWith(Annotation annotation)
   {
      return getAnnotated(particularElement(annotation), Optional::of);
   }

   @Override
   public Set<Declared> getDeclaredAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, annotationable -> convert(annotationable).toDeclared());
   }

   @Override
   public Set<Declared> getDeclaredAnnotatedWith(Annotation annotation)
   {
      return getAnnotated(particularElement(annotation), annotationable -> convert(annotationable).toDeclared());
   }

   @Override
   public Set<Class> getClassesAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, annotationable -> convert(annotationable).toClass());
   }

   @Override
   public Set<Class> getClassesAnnotatedWith(Annotation annotation)
   {
      return getAnnotated(particularElement(annotation), annotationable -> convert(annotationable).toClass());
   }

   @Override
   public Set<Enum> getEnumsAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, annotationable -> convert(annotationable).toEnum());
   }

   @Override
   public Set<Enum> getEnumsAnnotatedWith(Annotation annotation)
   {
      return getAnnotated(particularElement(annotation), annotationable -> convert(annotationable).toEnum());
   }

   @Override
   public Set<Interface> getInterfacesAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, annotationable -> convert(annotationable).toInterface());
   }

   @Override
   public Set<Interface> getInterfacesAnnotatedWith(Annotation annotation)
   {
      return getAnnotated(particularElement(annotation), annotationable -> convert(annotationable).toInterface());
   }

   @Override
   public Set<Record> getRecordsAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, annotationable -> convert(annotationable).toRecord());
   }

   @Override
   public Set<Record> getRecordsAnnotatedWith(Annotation annotation)
   {
      return getAnnotated(particularElement(annotation), annotationable -> convert(annotationable).toRecord());
   }

   @Override
   public Set<Field> getFieldsAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, annotationable -> convert(annotationable).toField());
   }

   @Override
   public Set<Field> getFieldsAnnotatedWith(Annotation annotation)
   {
      return getAnnotated(particularElement(annotation), annotationable -> convert(annotationable).toField());
   }

   @Override
   public Set<Parameter> getParametersAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, annotationable -> convert(annotationable).toParameter());
   }

   @Override
   public Set<Parameter> getParametersAnnotatedWith(Annotation annotation)
   {
      return getAnnotated(particularElement(annotation), annotationable -> convert(annotationable).toParameter());
   }

   @Override
   public Set<Method> getMethodsAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, annotationable -> convert(annotationable).toMethod());
   }

   @Override
   public Set<Method> getMethodsAnnotatedWith(Annotation annotation)
   {
      return getAnnotated(particularElement(annotation), annotationable -> convert(annotationable).toMethod());
   }

   @Override
   public Set<Constructor> getConstructorsAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, annotationable -> convert(annotationable).toConstructor());
   }

   @Override
   public Set<Constructor> getConstructorsAnnotatedWith(Annotation annotation)
   {
      return getAnnotated(particularElement(annotation), annotationable -> convert(annotationable).toConstructor());
   }

   @Override
   public Set<Annotation> getAnnotationsAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, annotationable -> convert(annotationable).toAnnotation());
   }

   @Override
   public Set<Annotation> getAnnotationsAnnotatedWith(Annotation annotation)
   {
      return getAnnotated(particularElement(annotation), annotationable -> convert(annotationable).toAnnotation());
   }

   @Override
   public Set<Package> getPackagesAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, annotationable -> convert(annotationable).toPackage());
   }

   @Override
   public Set<Package> gePackagesAnnotatedWith(Annotation annotation)
   {
      return getAnnotated(particularElement(annotation), annotationable -> convert(annotationable).toPackage());
   }

   @Override
   public Set<Generic> getGenericsAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, annotationable -> convert(annotationable).toGeneric());
   }

   @Override
   public Set<Generic> geGenericsAnnotatedWith(Annotation annotation)
   {
      return getAnnotated(particularElement(annotation), annotationable -> convert(annotationable).toGeneric());
   }

   @Override
   public Set<Module> getModulesAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, annotationable -> convert(annotationable).toModule());
   }

   @Override
   public Set<Module> geModulesAnnotatedWith(Annotation annotation)
   {
      return getAnnotated(particularElement(annotation), annotationable -> convert(annotationable).toModule());
   }

   @Override
   public Set<RecordComponent> getRecordComponentsAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, annotationable -> convert(annotationable).toRecordComponent());
   }

   @Override
   public Set<RecordComponent> geRecordComponentsAnnotatedWith(Annotation annotation)
   {
      return getAnnotated(particularElement(annotation), annotationable -> convert(annotationable).toRecordComponent());
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
   public void logAndRaiseErrorAt(Annotationable annotationable, String msg)
   {
      processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg, particularElement(annotationable));
   }

   @Override
   public void logInfoAt(Annotationable annotationable, String msg)
   {
      processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, msg, particularElement(annotationable));
   }

   @Override
   public void logWarningAt(Annotationable annotationable, String msg)
   {
      processingEnv.getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING, msg, particularElement(annotationable));
   }

   public AnnotationProcessingContext getApi()
   {
      return this;
   }

   @Override
   public List<Module> getModules()
   {
      return langModelContext.getModules();
   }

   @Override
   public Optional<Module> getModule(String name)
   {
      return langModelContext.getModule(name);
   }

   @Override
   public Module getModuleOrThrow(String name)
   {
      return langModelContext.getModuleOrThrow(name);
   }

   @Override
   public List<Package> getPackages(String qualifiedName)
   {
      return langModelContext.getPackages(qualifiedName);
   }

   @Override
   public List<Package> getPackages()
   {
      return langModelContext.getPackages();
   }

   @Override
   public Optional<Package> getPackage(String qualifiedModuleName, String qualifiedPackageName)
   {
      return langModelContext.getPackage(qualifiedModuleName, qualifiedPackageName);
   }

   @Override
   public Package getPackageOrThrow(String qualifiedModuleName, String qualifiedPackageName)
   {
      return langModelContext.getPackageOrThrow(qualifiedModuleName, qualifiedPackageName);
   }

   @Override
   public Optional<Package> getPackage(Module module, String qualifiedPackageName)
   {
      return langModelContext.getPackage(module, qualifiedPackageName);
   }

   @Override
   public Package getPackageOrThrow(Module module, String qualifiedPackageName)
   {
      return langModelContext.getPackageOrThrow(module, qualifiedPackageName);
   }

   @Override
   public LangModelConstants getConstants()
   {
      return langModelContext.getConstants();
   }

   @Override
   public Class withGenerics(Class aClass, Shadow... generics)
   {
      return langModelContext.withGenerics(aClass, generics);
   }

   @Override
   public Interface withGenerics(Interface anInterface, Shadow... generics)
   {
      return langModelContext.withGenerics(anInterface, generics);
   }

   @Override
   public Record withGenerics(Record aRecord, Shadow... generics)
   {
      return langModelContext.withGenerics(aRecord, generics);
   }

   @Override
   public Class erasure(Class aClass)
   {
      return langModelContext.erasure(aClass);
   }

   @Override
   public Interface erasure(Interface anInterface)
   {
      return langModelContext.erasure(anInterface);
   }

   @Override
   public Record erasure(Record aRecord)
   {
      return langModelContext.erasure(aRecord);
   }

   @Override
   public Array erasure(Array array)
   {
      return langModelContext.erasure(array);
   }

   @Override
   public Shadow erasure(Wildcard wildcard)
   {
      return langModelContext.erasure(wildcard);
   }

   @Override
   public Shadow erasure(Generic generic)
   {
      return langModelContext.erasure(generic);
   }

   @Override
   public Shadow erasure(Intersection intersection)
   {
      return langModelContext.erasure(intersection);
   }

   @Override
   public RecordComponent erasure(RecordComponent recordComponent)
   {
      return langModelContext.erasure(recordComponent);
   }

   @Override
   public Shadow erasure(Parameter parameter)
   {
      return langModelContext.erasure(parameter);
   }

   @Override
   public Shadow erasure(Field field)
   {
      return langModelContext.erasure(field);
   }

   @Override
   public Class interpolateGenerics(Class aClass)
   {
      return langModelContext.interpolateGenerics(aClass);
   }

   @Override
   public Interface interpolateGenerics(Interface anInterface)
   {
      return langModelContext.interpolateGenerics(anInterface);
   }

   @Override
   public Record interpolateGenerics(Record aRecord)
   {
      return langModelContext.interpolateGenerics(aRecord);
   }

   @Override
   public Array asArray(Array array)
   {
      return langModelContext.asArray(array);
   }

   @Override
   public Array asArray(Primitive primitive)
   {
      return langModelContext.asArray(primitive);
   }

   @Override
   public Array asArray(Declared declared)
   {
      return langModelContext.asArray(declared);
   }

   @Override
   public Array asArray(Intersection intersection)
   {
      return langModelContext.asArray(intersection);
   }

   @Override
   public Wildcard asExtendsWildcard(Array array)
   {
      return langModelContext.asExtendsWildcard(array);
   }

   @Override
   public Wildcard asSuperWildcard(Array array)
   {
      return langModelContext.asSuperWildcard(array);
   }

   @Override
   public Wildcard asExtendsWildcard(Declared array)
   {
      return langModelContext.asExtendsWildcard(array);
   }

   @Override
   public Wildcard asSuperWildcard(Declared array)
   {
      return langModelContext.asSuperWildcard(array);
   }

   @Override
   public List<Declared> getDeclared()
   {
      return langModelContext.getDeclared();
   }

   @Override
   public Optional<Declared> getDeclared(String qualifiedName)
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

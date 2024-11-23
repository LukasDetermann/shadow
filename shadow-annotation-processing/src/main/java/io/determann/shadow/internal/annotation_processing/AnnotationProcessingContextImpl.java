package io.determann.shadow.internal.annotation_processing;

import io.determann.shadow.api.Implementation;
import io.determann.shadow.api.annotation_processing.AP_Context;
import io.determann.shadow.api.annotation_processing.AP_DiagnosticContext;
import io.determann.shadow.api.lang_model.LM_Adapter;
import io.determann.shadow.api.lang_model.LM_Constants;
import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.LM_ContextImplementation;
import io.determann.shadow.api.lang_model.shadow.LM_Annotationable;
import io.determann.shadow.api.lang_model.shadow.structure.*;
import io.determann.shadow.api.lang_model.shadow.type.*;
import io.determann.shadow.api.shadow.C_Annotationable;
import io.determann.shadow.api.shadow.C_QualifiedNameable;
import io.determann.shadow.api.shadow.structure.C_Module;
import io.determann.shadow.api.shadow.type.C_Annotation;

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

import static io.determann.shadow.api.Operations.QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.lang_model.LM_Adapter.generalize;
import static io.determann.shadow.api.lang_model.LM_Adapter.particularElement;
import static java.lang.System.err;
import static java.lang.System.out;
import static java.util.stream.Collectors.toSet;

public class AnnotationProcessingContextImpl implements AP_Context,
                                                        LM_ContextImplementation
{
   private final ProcessingEnvironment processingEnv;
   private final RoundEnvironment roundEnv;
   private final int processingRound;
   private final LM_Context langModelContext;
   private BiConsumer<AP_Context, Throwable> exceptionHandler = (context, throwable) ->
   {
      StringWriter stringWriter = new StringWriter();
      PrintWriter printWriter = new PrintWriter(stringWriter);
      throwable.printStackTrace(printWriter);
      logAndRaiseError(stringWriter.toString());
      throw new RuntimeException(throwable);
   };
   private BiConsumer<AP_Context, AP_DiagnosticContext> diagnosticHandler = (context, diagnosticContext) ->
   {
      if (!context.isProcessingOver())
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
   private BiConsumer<AP_Context, String> systemOutHandler = (context, s) ->
   {
      if (!getProcessingEnv().toString().startsWith("javac"))
      {
         logWarning(s);
      }
   };
   private BiConsumer<AP_Context, String> systemErrorHandler = AP_Context::logAndRaiseError;


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
                                     return (C_Annotationable) generalize(getApi(), ((ExecutableElement) element));
                                  }
                                  return ((C_Annotationable) generalize(getApi(), element));
                               })
                          .filter(typeClass::isInstance)
                          .map(typeClass::cast)
                          .collect(toSet());
   }

   private <RESULT> Set<RESULT> getAnnotated(C_QualifiedNameable input, java.lang.Class<RESULT> resultClass)
   {
      if (input instanceof LM_Annotation annotationLangModel)
      {
         return getAnnotated(particularElement(annotationLangModel), resultClass);
      }
      return getAnnotated(requestOrThrow(input, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME), resultClass);
   }

   @Override
   public Set<LM_Annotationable> getAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, LM_Annotationable.class);
   }

   @Override
   public Set<LM_Annotationable> getAnnotatedWith(C_Annotation annotation)
   {
      return getAnnotated(annotation, LM_Annotationable.class);
   }

   @Override
   public Set<LM_Declared> getDeclaredAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, LM_Declared.class);
   }

   @Override
   public Set<LM_Declared> getDeclaredAnnotatedWith(C_Annotation annotation)
   {
      return getAnnotated(annotation, LM_Declared.class);
   }

   @Override
   public Set<LM_Class> getClassesAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, LM_Class.class);
   }

   @Override
   public Set<LM_Class> getClassesAnnotatedWith(C_Annotation annotation)
   {
      return getAnnotated(annotation, LM_Class.class);
   }

   @Override
   public Set<LM_Enum> getEnumsAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, LM_Enum.class);
   }

   @Override
   public Set<LM_Enum> getEnumsAnnotatedWith(C_Annotation annotation)
   {
      return getAnnotated(annotation, LM_Enum.class);
   }

   @Override
   public Set<LM_Interface> getInterfacesAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, LM_Interface.class);
   }

   @Override
   public Set<LM_Interface> getInterfacesAnnotatedWith(C_Annotation annotation)
   {
      return getAnnotated(annotation, LM_Interface.class);
   }

   @Override
   public Set<LM_Record> getRecordsAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, LM_Record.class);
   }

   @Override
   public Set<LM_Record> getRecordsAnnotatedWith(C_Annotation annotation)
   {
      return getAnnotated(annotation, LM_Record.class);
   }

   @Override
   public Set<LM_Field> getFieldsAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, LM_Field.class);
   }

   @Override
   public Set<LM_Field> getFieldsAnnotatedWith(C_Annotation annotation)
   {
      return getAnnotated(annotation, LM_Field.class);
   }

   @Override
   public Set<LM_Parameter> getParametersAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, LM_Parameter.class);
   }

   @Override
   public Set<LM_Parameter> getParametersAnnotatedWith(C_Annotation annotation)
   {
      return getAnnotated(annotation, LM_Parameter.class);
   }

   @Override
   public Set<LM_Method> getMethodsAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, LM_Method.class);
   }

   @Override
   public Set<LM_Method> getMethodsAnnotatedWith(C_Annotation annotation)
   {
      return getAnnotated(annotation, LM_Method.class);
   }

   @Override
   public Set<LM_Constructor> getConstructorsAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, LM_Constructor.class);
   }

   @Override
   public Set<LM_Constructor> getConstructorsAnnotatedWith(C_Annotation annotation)
   {
      return getAnnotated(annotation, LM_Constructor.class);
   }

   @Override
   public Set<LM_Annotation> getAnnotationsAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, LM_Annotation.class);
   }

   @Override
   public Set<LM_Annotation> getAnnotationsAnnotatedWith(C_Annotation annotation)
   {
      return getAnnotated(annotation, LM_Annotation.class);
   }

   @Override
   public Set<LM_Package> getPackagesAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, LM_Package.class);
   }

   @Override
   public Set<LM_Package> gePackagesAnnotatedWith(C_Annotation annotation)
   {
      return getAnnotated(annotation, LM_Package.class);
   }

   @Override
   public Set<LM_Generic> getGenericsAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, LM_Generic.class);
   }

   @Override
   public Set<LM_Generic> geGenericsAnnotatedWith(C_Annotation annotation)
   {
      return getAnnotated(annotation, LM_Generic.class);
   }

   @Override
   public Set<LM_Module> getModulesAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, LM_Module.class);
   }

   @Override
   public Set<LM_Module> geModulesAnnotatedWith(C_Annotation annotation)
   {
      return getAnnotated(annotation, LM_Module.class);
   }

   @Override
   public Set<LM_RecordComponent> getRecordComponentsAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, LM_RecordComponent.class);
   }

   @Override
   public Set<LM_RecordComponent> geRecordComponentsAnnotatedWith(C_Annotation annotation)
   {
      return getAnnotated(annotation, LM_RecordComponent.class);
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
   public void setExceptionHandler(BiConsumer<AP_Context, Throwable> exceptionHandler)
   {
      this.exceptionHandler = exceptionHandler;
   }

   @Override
   public BiConsumer<AP_Context, Throwable> getExceptionHandler()
   {
      return exceptionHandler;
   }

   @Override
   public void setDiagnosticHandler(BiConsumer<AP_Context, AP_DiagnosticContext> diagnosticHandler)
   {
      this.diagnosticHandler = diagnosticHandler;
   }

   @Override
   public BiConsumer<AP_Context, AP_DiagnosticContext> getDiagnosticHandler()
   {
      return diagnosticHandler;
   }

   @Override
   public void setSystemOutHandler(BiConsumer<AP_Context, String> systemOutHandler)
   {
      this.systemOutHandler = systemOutHandler;
   }

   @Override
   public BiConsumer<AP_Context, String> getSystemOutHandler()
   {
      return systemOutHandler;
   }

   @Override
   public void setSystemErrorHandler(BiConsumer<AP_Context, String> systemErrorHandler)
   {
      this.systemErrorHandler = systemErrorHandler;
   }

   @Override
   public BiConsumer<AP_Context, String> getSystemErrorHandler()
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
   public void logAndRaiseErrorAt(LM_Annotationable annotationable, String msg)
   {
      processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg, particularElement(annotationable));
   }

   @Override
   public void logInfoAt(LM_Annotationable annotationable, String msg)
   {
      processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, msg, particularElement(annotationable));
   }

   @Override
   public void logWarningAt(LM_Annotationable annotationable, String msg)
   {
      processingEnv.getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING, msg, particularElement(annotationable));
   }

   public AP_Context getApi()
   {
      return this;
   }

   @Override
   public List<LM_Module> getModules()
   {
      return langModelContext.getModules();
   }

   @Override
   public Optional<LM_Module> getModule(String name)
   {
      return langModelContext.getModule(name);
   }

   @Override
   public LM_Module getModuleOrThrow(String name)
   {
      return langModelContext.getModuleOrThrow(name);
   }

   @Override
   public List<LM_Package> getPackages(String qualifiedName)
   {
      return langModelContext.getPackages(qualifiedName);
   }

   @Override
   public List<LM_Package> getPackages()
   {
      return langModelContext.getPackages();
   }

   @Override
   public Optional<LM_Package> getPackage(String qualifiedModuleName, String qualifiedPackageName)
   {
      return langModelContext.getPackage(qualifiedModuleName, qualifiedPackageName);
   }

   @Override
   public LM_Package getPackageOrThrow(String qualifiedModuleName, String qualifiedPackageName)
   {
      return langModelContext.getPackageOrThrow(qualifiedModuleName, qualifiedPackageName);
   }

   @Override
   public Optional<LM_Package> getPackage(C_Module module, String qualifiedPackageName)
   {
      return langModelContext.getPackage(module, qualifiedPackageName);
   }

   @Override
   public LM_Package getPackageOrThrow(C_Module module, String qualifiedPackageName)
   {
      return langModelContext.getPackageOrThrow(module, qualifiedPackageName);
   }

   @Override
   public LM_Constants getConstants()
   {
      return langModelContext.getConstants();
   }

   @Override
   public List<LM_Declared> getDeclared()
   {
      return langModelContext.getDeclared();
   }

   @Override
   public Optional<LM_Declared> getDeclared(String qualifiedName)
   {
      return langModelContext.getDeclared(qualifiedName);
   }

   @Override
   public Types getTypes()
   {
      return LM_Adapter.getTypes(langModelContext);
   }

   @Override
   public Elements getElements()
   {
      return LM_Adapter.getElements(langModelContext);
   }

   @Override
   public Implementation getImplementation()
   {
      return langModelContext.getImplementation();
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

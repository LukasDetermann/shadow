package io.determann.shadow.internal.annotation_processing;

import io.determann.shadow.api.C;
import io.determann.shadow.api.annotation_processing.AP;
import io.determann.shadow.api.annotation_processing.DiagnosticContext;
import io.determann.shadow.api.lang_model.Constants;
import io.determann.shadow.api.lang_model.ContextImplementation;
import io.determann.shadow.api.lang_model.LM;
import io.determann.shadow.api.lang_model.adapter.Adapters;
import io.determann.shadow.api.query.Implementation;

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

import static io.determann.shadow.api.lang_model.adapter.Adapters.adapt;
import static io.determann.shadow.api.query.Operations.QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME;
import static io.determann.shadow.api.query.Provider.requestOrThrow;
import static java.lang.System.out;
import static java.util.stream.Collectors.toSet;

public class AnnotationProcessingContextImpl implements AP.Context,
                                                        ContextImplementation
{
   private final ProcessingEnvironment processingEnv;
   private final RoundEnvironment roundEnv;
   private final int processingRound;
   private final LM.Context langModelContext;
   private BiConsumer<AP.Context, Throwable> exceptionHandler = (context, throwable) ->
   {
      StringWriter stringWriter = new StringWriter();
      PrintWriter printWriter = new PrintWriter(stringWriter);
      throwable.printStackTrace(printWriter);
      logAndRaiseError(stringWriter.toString());
      throw new RuntimeException(throwable);
   };
   private BiConsumer<AP.Context, DiagnosticContext> diagnosticHandler = (context, diagnosticContext) ->
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
   private BiConsumer<AP.Context, String> systemOutHandler = (context, s) ->
   {
      if (!getProcessingEnv().toString().startsWith("javac"))
      {
         logWarning(s);
      }
   };

   public AnnotationProcessingContextImpl(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv, int processingRound)
   {
      this.langModelContext = Adapters.adapt(processingEnv.getTypeUtils(), processingEnv.getElementUtils());
      this.processingRound = processingRound;
      this.processingEnv = processingEnv;
      this.roundEnv = roundEnv;

      proxySystemOut();
   }

   private void proxySystemOut()
   {
      PrintStream printStream = new PrintStream(out, false, out.charset())
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
                                     return (C.Annotationable) Adapters.adapt(getApi(), ((ExecutableElement) element));
                                  }
                                  return ((C.Annotationable) Adapters.adapt(getApi(), element));
                               })
                          .filter(typeClass::isInstance)
                          .map(typeClass::cast)
                          .collect(toSet());
   }

   private <RESULT> Set<RESULT> getAnnotated(C.QualifiedNameable input, java.lang.Class<RESULT> resultClass)
   {
      if (input instanceof LM.Annotation annotationLangModel)
      {
         return getAnnotated(adapt(annotationLangModel).toTypeElement(), resultClass);
      }
      return getAnnotated(requestOrThrow(input, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME), resultClass);
   }

   @Override
   public Set<LM.Annotationable> getAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, LM.Annotationable.class);
   }

   @Override
   public Set<LM.Annotationable> getAnnotatedWith(C.Annotation annotation)
   {
      return getAnnotated(annotation, LM.Annotationable.class);
   }

   @Override
   public Set<LM.Declared> getDeclaredAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, LM.Declared.class);
   }

   @Override
   public Set<LM.Declared> getDeclaredAnnotatedWith(C.Annotation annotation)
   {
      return getAnnotated(annotation, LM.Declared.class);
   }

   @Override
   public Set<LM.Class> getClassesAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, LM.Class.class);
   }

   @Override
   public Set<LM.Class> getClassesAnnotatedWith(C.Annotation annotation)
   {
      return getAnnotated(annotation, LM.Class.class);
   }

   @Override
   public Set<LM.Enum> getEnumsAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, LM.Enum.class);
   }

   @Override
   public Set<LM.Enum> getEnumsAnnotatedWith(C.Annotation annotation)
   {
      return getAnnotated(annotation, LM.Enum.class);
   }

   @Override
   public Set<LM.Interface> getInterfacesAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, LM.Interface.class);
   }

   @Override
   public Set<LM.Interface> getInterfacesAnnotatedWith(C.Annotation annotation)
   {
      return getAnnotated(annotation, LM.Interface.class);
   }

   @Override
   public Set<LM.Record> getRecordsAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, LM.Record.class);
   }

   @Override
   public Set<LM.Record> getRecordsAnnotatedWith(C.Annotation annotation)
   {
      return getAnnotated(annotation, LM.Record.class);
   }

   @Override
   public Set<LM.Field> getFieldsAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, LM.Field.class);
   }

   @Override
   public Set<LM.Field> getFieldsAnnotatedWith(C.Annotation annotation)
   {
      return getAnnotated(annotation, LM.Field.class);
   }

   @Override
   public Set<LM.Parameter> getParametersAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, LM.Parameter.class);
   }

   @Override
   public Set<LM.Parameter> getParametersAnnotatedWith(C.Annotation annotation)
   {
      return getAnnotated(annotation, LM.Parameter.class);
   }

   @Override
   public Set<LM.Method> getMethodsAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, LM.Method.class);
   }

   @Override
   public Set<LM.Method> getMethodsAnnotatedWith(C.Annotation annotation)
   {
      return getAnnotated(annotation, LM.Method.class);
   }

   @Override
   public Set<LM.Constructor> getConstructorsAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, LM.Constructor.class);
   }

   @Override
   public Set<LM.Constructor> getConstructorsAnnotatedWith(C.Annotation annotation)
   {
      return getAnnotated(annotation, LM.Constructor.class);
   }

   @Override
   public Set<LM.Annotation> getAnnotationsAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, LM.Annotation.class);
   }

   @Override
   public Set<LM.Annotation> getAnnotationsAnnotatedWith(C.Annotation annotation)
   {
      return getAnnotated(annotation, LM.Annotation.class);
   }

   @Override
   public Set<LM.Package> getPackagesAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, LM.Package.class);
   }

   @Override
   public Set<LM.Package> gePackagesAnnotatedWith(C.Annotation annotation)
   {
      return getAnnotated(annotation, LM.Package.class);
   }

   @Override
   public Set<LM.Generic> getGenericsAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, LM.Generic.class);
   }

   @Override
   public Set<LM.Generic> geGenericsAnnotatedWith(C.Annotation annotation)
   {
      return getAnnotated(annotation, LM.Generic.class);
   }

   @Override
   public Set<LM.Module> getModulesAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, LM.Module.class);
   }

   @Override
   public Set<LM.Module> geModulesAnnotatedWith(C.Annotation annotation)
   {
      return getAnnotated(annotation, LM.Module.class);
   }

   @Override
   public Set<LM.RecordComponent> getRecordComponentsAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, LM.RecordComponent.class);
   }

   @Override
   public Set<LM.RecordComponent> geRecordComponentsAnnotatedWith(C.Annotation annotation)
   {
      return getAnnotated(annotation, LM.RecordComponent.class);
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
   public void setExceptionHandler(BiConsumer<AP.Context, Throwable> exceptionHandler)
   {
      this.exceptionHandler = exceptionHandler;
   }

   @Override
   public BiConsumer<AP.Context, Throwable> getExceptionHandler()
   {
      return exceptionHandler;
   }

   @Override
   public void setDiagnosticHandler(BiConsumer<AP.Context, DiagnosticContext> diagnosticHandler)
   {
      this.diagnosticHandler = diagnosticHandler;
   }

   @Override
   public BiConsumer<AP.Context, DiagnosticContext> getDiagnosticHandler()
   {
      return diagnosticHandler;
   }

   @Override
   public void setSystemOutHandler(BiConsumer<AP.Context, String> systemOutHandler)
   {
      this.systemOutHandler = systemOutHandler;
   }

   @Override
   public BiConsumer<AP.Context, String> getSystemOutHandler()
   {
      return systemOutHandler;
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
   public void logAndRaiseErrorAt(LM.Annotationable annotationable, String msg)
   {
      processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg, adapt(annotationable).toElement());
   }

   @Override
   public void logInfoAt(LM.Annotationable annotationable, String msg)
   {
      processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, msg, adapt(annotationable).toElement());
   }

   @Override
   public void logWarningAt(LM.Annotationable annotationable, String msg)
   {
      processingEnv.getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING, msg, adapt(annotationable).toElement());
   }

   public AP.Context getApi()
   {
      return this;
   }

   @Override
   public List<LM.Module> getModules()
   {
      return langModelContext.getModules();
   }

   @Override
   public Optional<LM.Module> getModule(String name)
   {
      return langModelContext.getModule(name);
   }

   @Override
   public LM.Module getModuleOrThrow(String name)
   {
      return langModelContext.getModuleOrThrow(name);
   }

   @Override
   public List<LM.Package> getPackage(String qualifiedName)
   {
      return langModelContext.getPackage(qualifiedName);
   }

   @Override
   public List<LM.Package> getPackages()
   {
      return langModelContext.getPackages();
   }

   @Override
   public Optional<LM.Package> getPackage(String qualifiedModuleName, String qualifiedPackageName)
   {
      return langModelContext.getPackage(qualifiedModuleName, qualifiedPackageName);
   }

   @Override
   public LM.Package getPackageOrThrow(String qualifiedModuleName, String qualifiedPackageName)
   {
      return langModelContext.getPackageOrThrow(qualifiedModuleName, qualifiedPackageName);
   }

   @Override
   public Optional<LM.Package> getPackage(C.Module module, String qualifiedPackageName)
   {
      return langModelContext.getPackage(module, qualifiedPackageName);
   }

   @Override
   public LM.Package getPackageOrThrow(C.Module module, String qualifiedPackageName)
   {
      return langModelContext.getPackageOrThrow(module, qualifiedPackageName);
   }

   @Override
   public Constants getConstants()
   {
      return langModelContext.getConstants();
   }

   @Override
   public List<LM.Declared> getDeclared()
   {
      return langModelContext.getDeclared();
   }

   @Override
   public Optional<LM.Declared> getDeclared(String qualifiedName)
   {
      return langModelContext.getDeclared(qualifiedName);
   }

   @Override
   public Types getTypes()
   {
      return adapt(langModelContext).toTypes();
   }

   @Override
   public Elements getElements()
   {
      return adapt(langModelContext).toElements();
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

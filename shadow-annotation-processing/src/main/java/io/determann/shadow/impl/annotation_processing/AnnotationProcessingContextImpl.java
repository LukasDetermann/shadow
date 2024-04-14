package io.determann.shadow.impl.annotation_processing;

import io.determann.shadow.api.Annotationable;
import io.determann.shadow.api.annotation_processing.AnnotationProcessingContext;
import io.determann.shadow.api.annotation_processing.DiagnosticContext;
import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Enum;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.Record;
import io.determann.shadow.api.shadow.*;
import io.determann.shadow.internal.lang_model.LangModelContextImpl;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.*;
import java.time.Duration;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static io.determann.shadow.api.annotation_processing.AnnotationProcessingAdapter.generalize;
import static io.determann.shadow.api.annotation_processing.AnnotationProcessingAdapter.particularElement;
import static io.determann.shadow.api.converter.Converter.convert;
import static java.lang.System.err;
import static java.lang.System.out;
import static java.util.stream.Collectors.toSet;

public class AnnotationProcessingContextImpl extends LangModelContextImpl implements AnnotationProcessingContext
{
   private final ProcessingEnvironment processingEnv;
   private final RoundEnvironment roundEnv;
   private final int processingRound;
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
      super(processingEnv.getTypeUtils(), processingEnv.getElementUtils());
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
      processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg, LangModelAdapter.particularElement(annotationable));
   }

   @Override
   public void logInfoAt(Annotationable annotationable, String msg)
   {
      processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, msg, LangModelAdapter.particularElement(annotationable));
   }

   @Override
   public void logWarningAt(Annotationable annotationable, String msg)
   {
      processingEnv.getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING, msg, LangModelAdapter.particularElement(annotationable));
   }

   public AnnotationProcessingContext getApi()
   {
      return this;
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

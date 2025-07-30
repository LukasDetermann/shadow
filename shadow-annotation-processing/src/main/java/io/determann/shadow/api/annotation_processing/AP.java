package io.determann.shadow.api.annotation_processing;

import io.determann.shadow.api.C;
import io.determann.shadow.api.lang_model.LM;
import io.determann.shadow.internal.annotation_processing.AnnotationProcessingContextImpl;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.time.Instant;
import java.util.Set;
import java.util.function.BiConsumer;

public interface AP
{
   /**
    * Extend this class and implement {@link #process(Context)} for the easiest way to use the {@link Context}.
    * <p>
    * AnnotationProcessing happens in rounds. A round contains every processor. A new Round is triggered when
    * an Annotation processor generates new sources.
    *
    * @see Context
    * @see javax.annotation.processing.Processor for detailed dokumentation on annotationProcessing without the shadowApi
    */
   abstract class Processor
         extends AbstractProcessor
   {
      private int processingRound = 0;

      @Override
      public Set<String> getSupportedAnnotationTypes()
      {
         return Set.of("*");
      }

      /**
       * Needs to be at least the version of the compiled code. otherwise the processor will be ignored
       */
      @Override
      public SourceVersion getSupportedSourceVersion()
      {
         return SourceVersion.latest();
      }

      /**
       * Intercepts the process call and initializes the {@link Context}.
       * If you want to use the {@link Context} don't override this method.
       */
      @Override
      public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
      {
         Instant start = Instant.now();
         Context api = new AnnotationProcessingContextImpl(processingEnv, roundEnv, processingRound);
         try
         {
            process(api);
         }
         //the compiler can crash when an uncaught exception is thrown. so it is just printed here and will raise an error
         //using the proxied err outputStream in the ShadowApi
         catch (Exception t)
         {
            if (api.getExceptionHandler() != null)
            {
               api.getExceptionHandler().accept(api, t);
            }
         }
         if (api.getDiagnosticHandler() != null)
         {
            String processorName = getClass().isAnonymousClass() ? "anonymousProcessor" : getClass().getSimpleName();
            api.getDiagnosticHandler().accept(api, new DiagnosticContext(api, processorName, processingRound, start, Instant.now()));
         }
         processingRound++;

         //claiming annotations is kinda useless
         return false;
      }

      /**
       * Override this method if you want to use the {@link Context}. This Method can be called many times during compilation.
       *
       * @see Context
       * @see javax.annotation.processing.Processor for detailed doku on annotationProcessing without the shadowApi
       */
      public abstract void process(Context annotationProcessingContext) throws Exception;
   }

   /**
    * This is the core class for a lightweight wrapper around the java annotationProcessor api. The {@link Context} is transient. Meaning you can
    * transition between using the shadow and the java annotation processor api from line to line if you so wish.
    * <br><br>
    *
    * <h2>Usage:</h2>
    *
    * <ul>
    *    <li>get anything that is annotated {@link #getAnnotatedWith(String)}</li>
    *    <li>get already compiled sources {@link #getDeclaredOrThrow(String)} for example</li>
    *    <li>get constants {@link #getConstants()}</li>
    *    <li>log using {@link #logAndRaiseError(String)} or log at {@link #logAndRaiseErrorAt(LM.Annotationable, String)}</li>
    * </ul>
    *
    * @see Processor
    * @see C.Type
    */
   interface Context
         extends LM.Context
   {
      /**
       * Looks up annotated elements in currently compiled code. <br>
       * Annotation processing happens in rounds.
       * If a processor generates sources the next round will contain only these.
       *
       * @see #writeAndCompileSourceFile(String, String)
       * @see #writeClassFile(String, String)
       */
      Set<LM.Annotationable> getAnnotatedWith(String qualifiedAnnotation);

      /**
       * Looks up annotated elements in currently compiled code. <br>
       * Annotation processing happens in rounds.
       * If a processor generates sources the next round will contain only these.
       *
       * @see #writeAndCompileSourceFile(String, String)
       * @see #writeClassFile(String, String)
       */
      Set<LM.Annotationable> getAnnotatedWith(C.Annotation annotation);

      /**
       * Looks up annotated elements in currently compiled code. <br>
       * Annotation processing happens in rounds.
       * If a processor generates sources the next round will contain only these.
       *
       * @see #writeAndCompileSourceFile(String, String)
       * @see #writeClassFile(String, String)
       */
      Set<LM.Declared> getDeclaredAnnotatedWith(String qualifiedAnnotation);

      /**
       * Looks up annotated elements in currently compiled code. <br>
       * Annotation processing happens in rounds.
       * If a processor generates sources the next round will contain only these.
       *
       * @see #writeAndCompileSourceFile(String, String)
       * @see #writeClassFile(String, String)
       */
      Set<LM.Declared> getDeclaredAnnotatedWith(C.Annotation annotation);

      /**
       * Looks up annotated elements in currently compiled code. <br>
       * Annotation processing happens in rounds.
       * If a processor generates sources the next round will contain only these.
       *
       * @see #writeAndCompileSourceFile(String, String)
       * @see #writeClassFile(String, String)
       */
      Set<LM.Class> getClassesAnnotatedWith(String qualifiedAnnotation);

      /**
       * Looks up annotated elements in currently compiled code. <br>
       * Annotation processing happens in rounds.
       * If a processor generates sources the next round will contain only these.
       *
       * @see #writeAndCompileSourceFile(String, String)
       * @see #writeClassFile(String, String)
       */
      Set<LM.Class> getClassesAnnotatedWith(C.Annotation annotation);

      /**
       * Looks up annotated elements in currently compiled code. <br>
       * Annotation processing happens in rounds.
       * If a processor generates sources the next round will contain only these.
       *
       * @see #writeAndCompileSourceFile(String, String)
       * @see #writeClassFile(String, String)
       */
      Set<LM.Enum> getEnumsAnnotatedWith(String qualifiedAnnotation);

      /**
       * Looks up annotated elements in currently compiled code. <br>
       * Annotation processing happens in rounds.
       * If a processor generates sources the next round will contain only these.
       *
       * @see #writeAndCompileSourceFile(String, String)
       * @see #writeClassFile(String, String)
       */
      Set<LM.Enum> getEnumsAnnotatedWith(C.Annotation annotation);

      /**
       * Looks up annotated elements in currently compiled code. <br>
       * Annotation processing happens in rounds.
       * If a processor generates sources the next round will contain only these.
       *
       * @see #writeAndCompileSourceFile(String, String)
       * @see #writeClassFile(String, String)
       */
      Set<LM.Interface> getInterfacesAnnotatedWith(String qualifiedAnnotation);

      /**
       * Looks up annotated elements in currently compiled code. <br>
       * Annotation processing happens in rounds.
       * If a processor generates sources the next round will contain only these.
       *
       * @see #writeAndCompileSourceFile(String, String)
       * @see #writeClassFile(String, String)
       */
      Set<LM.Interface> getInterfacesAnnotatedWith(C.Annotation annotation);

      /**
       * Looks up annotated elements in currently compiled code. <br>
       * Annotation processing happens in rounds.
       * If a processor generates sources the next round will contain only these.
       *
       * @see #writeAndCompileSourceFile(String, String)
       * @see #writeClassFile(String, String)
       */
      Set<LM.Record> getRecordsAnnotatedWith(String qualifiedAnnotation);

      /**
       * Looks up annotated elements in currently compiled code. <br>
       * Annotation processing happens in rounds.
       * If a processor generates sources the next round will contain only these.
       *
       * @see #writeAndCompileSourceFile(String, String)
       * @see #writeClassFile(String, String)
       */
      Set<LM.Record> getRecordsAnnotatedWith(C.Annotation annotation);

      /**
       * Looks up annotated elements in currently compiled code. <br>
       * Annotation processing happens in rounds.
       * If a processor generates sources the next round will contain only these.
       *
       * @see #writeAndCompileSourceFile(String, String)
       * @see #writeClassFile(String, String)
       */
      Set<LM.Field> getFieldsAnnotatedWith(String qualifiedAnnotation);

      /**
       * Looks up annotated elements in currently compiled code. <br>
       * Annotation processing happens in rounds.
       * If a processor generates sources the next round will contain only these.
       *
       * @see #writeAndCompileSourceFile(String, String)
       * @see #writeClassFile(String, String)
       */
      Set<LM.Field> getFieldsAnnotatedWith(C.Annotation annotation);

      /**
       * Looks up annotated elements in currently compiled code. <br>
       * Annotation processing happens in rounds.
       * If a processor generates sources the next round will contain only these.
       *
       * @see #writeAndCompileSourceFile(String, String)
       * @see #writeClassFile(String, String)
       */
      Set<LM.Parameter> getParametersAnnotatedWith(String qualifiedAnnotation);

      /**
       * Looks up annotated elements in currently compiled code. <br>
       * Annotation processing happens in rounds.
       * If a processor generates sources the next round will contain only these.
       *
       * @see #writeAndCompileSourceFile(String, String)
       * @see #writeClassFile(String, String)
       */
      Set<LM.Parameter> getParametersAnnotatedWith(C.Annotation annotation);

      /**
       * Looks up annotated elements in currently compiled code. <br>
       * Annotation processing happens in rounds.
       * If a processor generates sources the next round will contain only these.
       *
       * @see #writeAndCompileSourceFile(String, String)
       * @see #writeClassFile(String, String)
       */
      Set<LM.Method> getMethodsAnnotatedWith(String qualifiedAnnotation);

      /**
       * Looks up annotated elements in currently compiled code. <br>
       * Annotation processing happens in rounds.
       * If a processor generates sources the next round will contain only these.
       *
       * @see #writeAndCompileSourceFile(String, String)
       * @see #writeClassFile(String, String)
       */
      Set<LM.Method> getMethodsAnnotatedWith(C.Annotation annotation);

      /**
       * Looks up annotated elements in currently compiled code. <br>
       * Annotation processing happens in rounds.
       * If a processor generates sources the next round will contain only these.
       *
       * @see #writeAndCompileSourceFile(String, String)
       * @see #writeClassFile(String, String)
       */
      Set<LM.Constructor> getConstructorsAnnotatedWith(String qualifiedAnnotation);

      /**
       * Looks up annotated elements in currently compiled code. <br>
       * Annotation processing happens in rounds.
       * If a processor generates sources the next round will contain only these.
       *
       * @see #writeAndCompileSourceFile(String, String)
       * @see #writeClassFile(String, String)
       */
      Set<LM.Constructor> getConstructorsAnnotatedWith(C.Annotation annotation);

      /**
       * Looks up annotated elements in currently compiled code. <br>
       * Annotation processing happens in rounds.
       * If a processor generates sources the next round will contain only these.
       *
       * @see #writeAndCompileSourceFile(String, String)
       * @see #writeClassFile(String, String)
       */
      Set<LM.Annotation> getAnnotationsAnnotatedWith(String qualifiedAnnotation);

      /**
       * Looks up annotated elements in currently compiled code. <br>
       * Annotation processing happens in rounds.
       * If a processor generates sources the next round will contain only these.
       *
       * @see #writeAndCompileSourceFile(String, String)
       * @see #writeClassFile(String, String)
       */
      Set<LM.Annotation> getAnnotationsAnnotatedWith(C.Annotation annotation);

      /**
       * Looks up annotated elements in currently compiled code. <br>
       * Annotation processing happens in rounds.
       * If a processor generates sources the next round will contain only these.
       *
       * @see #writeAndCompileSourceFile(String, String)
       * @see #writeClassFile(String, String)
       */
      Set<LM.Package> getPackagesAnnotatedWith(String qualifiedAnnotation);

      /**
       * Looks up annotated elements in currently compiled code. <br>
       * Annotation processing happens in rounds.
       * If a processor generates sources the next round will contain only these.
       *
       * @see #writeAndCompileSourceFile(String, String)
       * @see #writeClassFile(String, String)
       */
      Set<LM.Package> gePackagesAnnotatedWith(C.Annotation annotation);

      /**
       * Looks up annotated elements in currently compiled code. <br>
       * Annotation processing happens in rounds.
       * If a processor generates sources the next round will contain only these.
       *
       * @see #writeAndCompileSourceFile(String, String)
       * @see #writeClassFile(String, String)
       */
      Set<LM.Generic> getGenericsAnnotatedWith(String qualifiedAnnotation);

      /**
       * Looks up annotated elements in currently compiled code. <br>
       * Annotation processing happens in rounds.
       * If a processor generates sources the next round will contain only these.
       *
       * @see #writeAndCompileSourceFile(String, String)
       * @see #writeClassFile(String, String)
       */
      Set<LM.Generic> geGenericsAnnotatedWith(C.Annotation annotation);

      /**
       * Looks up annotated elements in currently compiled code. <br>
       * Annotation processing happens in rounds.
       * If a processor generates sources the next round will contain only these.
       *
       * @see #writeAndCompileSourceFile(String, String)
       * @see #writeClassFile(String, String)
       */
      Set<LM.Module> getModulesAnnotatedWith(String qualifiedAnnotation);

      /**
       * Looks up annotated elements in currently compiled code. <br>
       * Annotation processing happens in rounds.
       * If a processor generates sources the next round will contain only these.
       *
       * @see #writeAndCompileSourceFile(String, String)
       * @see #writeClassFile(String, String)
       */
      Set<LM.Module> geModulesAnnotatedWith(C.Annotation annotation);

      /**
       * Looks up annotated elements in currently compiled code. <br>
       * Annotation processing happens in rounds.
       * If a processor generates sources the next round will contain only these.
       *
       * @see #writeAndCompileSourceFile(String, String)
       * @see #writeClassFile(String, String)
       */
      Set<LM.RecordComponent> getRecordComponentsAnnotatedWith(String qualifiedAnnotation);

      /**
       * Looks up annotated elements in currently compiled code. <br>
       * Annotation processing happens in rounds.
       * If a processor generates sources the next round will contain only these.
       *
       * @see #writeAndCompileSourceFile(String, String)
       * @see #writeClassFile(String, String)
       */
      Set<LM.RecordComponent> geRecordComponentsAnnotatedWith(C.Annotation annotation);

      /**
       * the created file will be registered for the next annotation processor round. writes .java files
       */
      void writeAndCompileSourceFile(String qualifiedName, String content);

      /**
       * the created file will be registered for the next annotation processor round. writes .class files
       */
      void writeClassFile(String qualifiedName, String content);

      /**
       * the created file will NOT be registered for the next annotation processor round. writes anything
       */
      void writeResource(StandardLocation location, String moduleAndPkg, String relativPath, String content);

      FileObject readResource(StandardLocation location, String moduleAndPkg, String relativPath) throws IOException;

      /**
       * Last round of annotation processing
       */
      boolean isProcessingOver();

      /**
       * First round of annotation processing
       */
      boolean isFirstRound();

      /**
       * starts at 0
       */
      int getProcessingRound();

      /**
       * Consumer to handle exceptions that occur in this annotation processor.
       * If you want the compilation to stop because of it just throw any expedition.
       */
      void setExceptionHandler(BiConsumer<Context, Throwable> exceptionHandler);

      /**
       * @see #setExceptionHandler(BiConsumer)
       */
      BiConsumer<Context, Throwable> getExceptionHandler();

      /**
       * Executed at the end of each round.
       * When the processing is over each Processor gets called one more time with {@link #isProcessingOver()} = true.
       */
      void setDiagnosticHandler(BiConsumer<Context, DiagnosticContext> diagnosticHandler);

      /**
       * @see #setDiagnosticHandler(BiConsumer)
       */
      BiConsumer<Context, DiagnosticContext> getDiagnosticHandler();

      /**
       * Some {@link javax.tools.Tool} don't support {@link System#out}. By default, it is proxied and redirected to the logger as warning
       */
      void setSystemOutHandler(BiConsumer<Context, String> systemOutHandler);

      /**
       * @see #setSystemOutHandler(BiConsumer)
       */
      BiConsumer<Context, String> getSystemOutHandler();

      void logAndRaiseError(String msg);

      void logInfo(String msg);

      void logWarning(String msg);

      void logAndRaiseErrorAt(LM.Annotationable annotationable, String msg);

      void logInfoAt(LM.Annotationable annotationable, String msg);

      void logWarningAt(LM.Annotationable annotationable, String msg);
   }
}

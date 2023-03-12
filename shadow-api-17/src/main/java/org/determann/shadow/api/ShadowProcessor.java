package org.determann.shadow.api;

import org.determann.shadow.impl.ProcessorDiagnosticsRenderer;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.time.Duration;
import java.time.Instant;
import java.util.Set;

/**
 * Extend this class and implement {@link #process(ShadowApi)} for the easiest way to use the {@link ShadowApi}.
 * <p>
 * AnnotationProcessing happens in rounds. A round contains every processor. A new Round is triggered when
 * an Annotation processor generates new sources.
 *
 * @see ShadowApi
 * @see javax.annotation.processing.Processor for detailed dokumentation on annotationProcessing without the shadowApi
 */
public abstract class ShadowProcessor extends AbstractProcessor
{
   private int processingRoundNumber = 0;

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
    * Intercepts the process call and initializes the {@link ShadowApi}.
    * If you want to use the {@link ShadowApi} don't override this method.
    */
   @Override
   public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
   {
      Instant start = Instant.now();
      //noinspection OverlyBroadCatchBlock
      try
      {
         process(ShadowApi.create(processingEnv, roundEnv, processingRoundNumber));
      }
      //the compiler can crash when an uncaught exception is thrown. so it is just printed here and will raise an error
      //using the proxied err outputStream in the ShadowApi
      catch (Throwable t)
      {
         //noinspection CallToPrintStackTrace
         t.printStackTrace();
         throw new RuntimeException(t);
      }
      if (!roundEnv.processingOver())
      {
         printDiagnostics(start);
      }
      //claiming annotations is kinda useless
      return false;
   }

   private void printDiagnostics(Instant start)
   {
      processingEnv.getMessager()
                   .printMessage(Diagnostic.Kind.MANDATORY_WARNING,
                                 ProcessorDiagnosticsRenderer.render(getClass().getSimpleName(),
                                                                     ++processingRoundNumber,
                                                                     Duration.between(start, Instant.now())));
   }

   /**
    * Override this method if you want to use the {@link ShadowApi}. This Method can be called many times during compilation.
    *
    * @see ShadowApi
    * @see javax.annotation.processing.Processor for detailed doku on annotationProcessing without the shadowApi
    */
   public abstract void process(ShadowApi shadowApi) throws Exception;
}

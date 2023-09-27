package io.determann.shadow.api;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
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
   private int processingRound = 0;
   private ShadowApi api;

   @Override
   public Set<String> getSupportedAnnotationTypes()
   {
      return new HashSet<>(Collections.singletonList("*"));
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
      api = ShadowApi.create(api, processingEnv, roundEnv, processingRound);
      //noinspection OverlyBroadCatchBlock
      try
      {
         process(api);
      }
      //the compiler can crash when an uncaught exception is thrown. so it is just printed here and will raise an error
      //using the proxied err outputStream in the ShadowApi
      catch (Throwable t)
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
    * Override this method if you want to use the {@link ShadowApi}. This Method can be called many times during compilation.
    *
    * @see ShadowApi
    * @see javax.annotation.processing.Processor for detailed doku on annotationProcessing without the shadowApi
    */
   public abstract void process(ShadowApi shadowApi) throws Exception;
}

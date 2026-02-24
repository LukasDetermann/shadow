package io.determann.shadow.api.annotation_processing;

import io.determann.shadow.internal.annotation_processing.ApContextImpl;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.time.Instant;
import java.util.Set;

/**
 * Extend this class and implement {@link #process(Context)} for the easiest way to use the {@link Context}.
 * <p>
 * AnnotationProcessing happens in rounds. A round contains every processor. A new Round is triggered when
 * an Annotation processor generates new sources.
 *
 * @see Context
 * @see javax.annotation.processing.Processor for detailed dokumentation on annotationProcessing without the shadowApi
 */
public abstract class Processor
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
    */
   @Override
   public final boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
   {
      Instant start = Instant.now();
      Context api = new ApContextImpl(processingEnv, roundEnv, processingRound);
      try
      {
         process(api);
      }
      //the compiler can crash when an uncaught exception is thrown. so they are handled here. the default handler Logs and raises an error
      catch (Exception t)
      {
         api.getExceptionHandler().accept(api, t);
      }
      String processorName = getClass().isAnonymousClass() ? "anonymousProcessor" : getClass().getSimpleName();
      api.getDiagnosticHandler().accept(api, new DiagnosticContext(api, processorName, processingRound, start, Instant.now()));
      processingRound++;

      //claiming annotations is kinda useless
      return false;
   }

   /**
    * Entry Point for Annotation Processing. This Method can be called many times during compilation.
    *
    * @see Context
    * @see javax.annotation.processing.Processor for detailed doku on annotationProcessing without this Api
    */
   public abstract void process(Context annotationProcessingContext) throws Exception;
}

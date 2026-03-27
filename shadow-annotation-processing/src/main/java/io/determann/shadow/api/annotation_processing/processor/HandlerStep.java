package io.determann.shadow.api.annotation_processing.processor;

import org.jetbrains.annotations.Contract;

import java.util.function.BiConsumer;

public interface HandlerStep extends ProcessorConfiguration
{
   @Contract(value = "_ -> new", pure = true)
   HandlerStep withExceptionHandler(BiConsumer<SimpleContext, Throwable> exceptionHandler);

   /**
    * Executed at the end of each round.
    * When the processing is over each Processor gets called one more time with {@link Context#isProcessingOver()} = true.
    */
   @Contract(value = "_ -> new", pure = true)
   HandlerStep withDiagnosticHandler(BiConsumer<SimpleContext, DiagnosticContext> diagnosticHandler);

   /**
    * Some {@link javax.tools.Tool} don't support {@link System#out}. By default, it is proxied and redirected to the logger as warning
    */
   @Contract(value = "_ -> new", pure = true)
   HandlerStep withSystemOutHandler(BiConsumer<SimpleContext, String> systemOutHandler);
}
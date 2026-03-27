package io.determann.shadow.internal.annotation_processing.processor;

import io.determann.shadow.api.annotation_processing.processor.DiagnosticContext;
import io.determann.shadow.api.annotation_processing.processor.ProcessorConfiguration;
import io.determann.shadow.api.annotation_processing.processor.SimpleContext;
import io.determann.shadow.api.annotation_processing.test.ProcessingCallback;

import javax.lang.model.SourceVersion;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

public interface ProcessorConfigurationImpl<OPTIONS>
      extends ProcessorConfiguration
{
   Set<String> getSupportedOptions();

   Optional<Function<Map<String, String>, OPTIONS>> getOptionsMapper();

   SourceVersion getSourceVersion();

   ProcessingCallback<? super SimpleContext> getProcessingCallable();

   BiConsumer<SimpleContext, Throwable> getExceptionHandler();

   BiConsumer<SimpleContext, DiagnosticContext> getDiagnosticHandler();

   BiConsumer<SimpleContext, String> getSystemOutHandler();
}
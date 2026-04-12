package io.determann.shadow.api.annotation_processing.processor;

import org.jetbrains.annotations.Contract;

import java.util.function.Supplier;

public interface OptionConfigurationStep extends ProcessorStep<ProcessingCallback<SimpleContext>>
{
   @Contract(value = "_ -> new", pure = true)
   <OPTIONS> OptionStep<OPTIONS> withOptions(Supplier<OPTIONS> optionsSupplier);
}

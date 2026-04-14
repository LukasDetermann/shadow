package com.derivandi.internal.processor;

import com.derivandi.api.processor.*;

import javax.lang.model.SourceVersion;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class ProcessorBuilderImpl
      implements ProcessorBuilder
{
   private SourceVersion sourceVersion = SourceVersion.latest();
   private Callable<? extends SimpleContext> processingCallable;

   public ProcessorBuilderImpl() {}

   private ProcessorBuilderImpl(ProcessorBuilderImpl other)
   {
      this.sourceVersion = other.sourceVersion;
      this.processingCallable = other.processingCallable;
   }

   @Override
   public OptionConfigurationStep withSupportUpTo(SourceVersion sourceVersion)
   {
      Objects.requireNonNull(sourceVersion);
      ProcessorBuilderImpl copy = new ProcessorBuilderImpl(this);
      copy.sourceVersion = sourceVersion;
      return copy;
   }

   @Override
   public <OPTIONS> OptionStep<OPTIONS> withOptions(Supplier<OPTIONS> optionsSupplier)
   {
      Objects.requireNonNull(optionsSupplier);
      return new OptionStepImpl<>(sourceVersion, optionsSupplier);
   }

   @Override
   public HandlerStep process(ProcessingCallback<SimpleContext> processingCallback)
   {
      Objects.requireNonNull(processingCallback);
      return new HandlerStepImpl<>(sourceVersion, processingCallback);
   }
}

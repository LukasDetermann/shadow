package io.determann.shadow.api.annotation_processing.processor;

import org.jetbrains.annotations.Contract;

import javax.lang.model.SourceVersion;

public interface SourceVersionStep
      extends OptionConfigurationStep
{
   @Contract(value = "_ -> new", pure = true)
   OptionConfigurationStep withSupportUpTo(SourceVersion sourceVersion);

   @Contract(value = "-> new", pure = true)
   default OptionConfigurationStep withSupportForAllCurrentAndFuture()
   {
      return withSupportUpTo(SourceVersion.latest());
   }
}

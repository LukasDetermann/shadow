package io.determann.shadow.api.annotation_processing.dsl.requires;

import org.jetbrains.annotations.Contract;

public interface RequiresModifierStep
      extends RequiresNameStep
{
   @Contract(value = " -> new", pure = true)
   RequiresNameStep transitive();

   @Contract(value = " -> new", pure = true)
   RequiresNameStep static_();
}

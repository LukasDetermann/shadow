package io.determann.shadow.api.dsl.requires;

public interface RequiresModifierStep
      extends RequiresNameStep
{
   RequiresNameStep transitive();

   RequiresNameStep static_();
}

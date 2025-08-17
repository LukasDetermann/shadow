package io.determann.shadow.api.dsl.parameter;

import org.jetbrains.annotations.Contract;

public interface ParameterModifierStep
      extends ParameterTypeStep
{
   @Contract(value = " -> new", pure = true)
   ParameterTypeStep final_();
}

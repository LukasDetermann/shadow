package io.determann.shadow.api.dsl.parameter;

import io.determann.shadow.api.dsl.VariableTypeRenderable;
import org.jetbrains.annotations.Contract;

public interface ParameterTypeStep
{
   @Contract(value = "_ -> new", pure = true)
   ParameterNameStep type(String type);

   @Contract(value = "_ -> new", pure = true)
   ParameterNameStep type(VariableTypeRenderable variableType);
}

package com.derivandi.api.dsl.parameter;

import com.derivandi.api.dsl.VariableTypeRenderable;
import org.jetbrains.annotations.Contract;

public interface ParameterTypeStep
{
   @Contract(value = "_ -> new", pure = true)
   ParameterNameStep type(String type);

   @Contract(value = "_ -> new", pure = true)
   ParameterNameStep type(VariableTypeRenderable variableType);
}

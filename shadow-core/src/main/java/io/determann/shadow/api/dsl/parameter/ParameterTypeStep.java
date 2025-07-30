package io.determann.shadow.api.dsl.parameter;

import io.determann.shadow.api.dsl.VariableTypeRenderable;

public interface ParameterTypeStep
{
   ParameterNameStep type(String type);

   ParameterNameStep type(VariableTypeRenderable variableType);
}

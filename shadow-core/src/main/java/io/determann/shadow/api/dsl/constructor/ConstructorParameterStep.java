package io.determann.shadow.api.dsl.constructor;

import io.determann.shadow.api.dsl.parameter.ParameterRenderable;
import io.determann.shadow.api.shadow.structure.C_Parameter;

public interface ConstructorParameterStep extends ConstructorThrowsStep
{
   ConstructorParameterStep parameter(String... parameter);

   ConstructorParameterStep parameter(C_Parameter... parameter);

   ConstructorParameterStep parameter(ParameterRenderable... parameter);
}

package io.determann.shadow.api.dsl.method;

import io.determann.shadow.api.dsl.parameter.ParameterRenderable;
import io.determann.shadow.api.shadow.structure.C_Parameter;

public interface MethodParameterStep extends MethodThrowsStep
{
   MethodParameterStep parameter(String... parameter);

   MethodParameterStep parameter(C_Parameter... parameter);

   MethodParameterStep parameter(ParameterRenderable... parameter);
}

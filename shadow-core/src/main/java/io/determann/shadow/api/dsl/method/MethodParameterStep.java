package io.determann.shadow.api.dsl.method;

import io.determann.shadow.api.dsl.parameter.ParameterRenderable;

import java.util.Arrays;
import java.util.List;

public interface MethodParameterStep extends MethodThrowsStep
{
   MethodParameterStep parameter(String... parameter);

   default MethodParameterStep parameter(ParameterRenderable... parameter)
   {
      return parameter(Arrays.asList(parameter));
   }

   MethodParameterStep parameter(List<? extends ParameterRenderable> parameter);
}

package io.determann.shadow.api.dsl.constructor;

import io.determann.shadow.api.dsl.parameter.ParameterRenderable;

import java.util.Arrays;
import java.util.List;

public interface ConstructorParameterStep extends ConstructorThrowsStep
{
   ConstructorParameterStep parameter(String... parameter);

   default ConstructorParameterStep parameter(ParameterRenderable... parameter)
   {
      return parameter(Arrays.asList(parameter));
   }

   ConstructorParameterStep parameter(List<? extends ParameterRenderable> parameter);
}

package io.determann.shadow.api.dsl.enum_constant;

import io.determann.shadow.api.dsl.parameter.ParameterRenderable;

import java.util.Arrays;
import java.util.List;

public interface EnumConstantParameterStep
      extends EnumConstantBodyStep
{
   EnumConstantParameterStep parameter(String... parameter);

   default EnumConstantParameterStep parameter(ParameterRenderable... parameter)
   {
      return parameter(Arrays.asList(parameter));
   }

   EnumConstantParameterStep parameter(List<? extends ParameterRenderable> parameter);
}
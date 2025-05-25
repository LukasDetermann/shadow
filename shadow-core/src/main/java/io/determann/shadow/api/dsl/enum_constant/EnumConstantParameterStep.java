package io.determann.shadow.api.dsl.enum_constant;

import io.determann.shadow.api.dsl.parameter.ParameterRenderable;
import io.determann.shadow.api.shadow.structure.C_Parameter;

public interface EnumConstantParameterStep
      extends EnumConstantBodyStep
{
   EnumConstantParameterStep parameter(String... parameter);

   EnumConstantParameterStep parameter(C_Parameter... parameter);

   EnumConstantParameterStep parameter(ParameterRenderable... parameter);
}

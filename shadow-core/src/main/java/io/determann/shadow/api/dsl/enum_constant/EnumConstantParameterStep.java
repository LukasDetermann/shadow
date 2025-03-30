package io.determann.shadow.api.dsl.enum_constant;

import io.determann.shadow.api.shadow.structure.C_Parameter;

public interface EnumConstantParameterStep
      extends EnumConstantBodyStep
{
   EnumConstantParameterStep parameter(String... parameter);

   EnumConstantParameterStep parameter(C_Parameter... parameter);
}

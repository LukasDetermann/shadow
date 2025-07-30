package io.determann.shadow.api.dsl.enum_;

import io.determann.shadow.api.shadow.structure.C_EnumConstant;

import java.util.Arrays;
import java.util.List;

public interface EnumEnumConstantStep
      extends EnumBodyStep
{
   EnumEnumConstantStep enumConstant(String... enumConstant);

   default EnumEnumConstantStep enumConstant(C_EnumConstant... enumConstant)
   {
      return enumConstant(Arrays.asList(enumConstant));
   }

   EnumEnumConstantStep enumConstant(List<? extends C_EnumConstant> enumConstant);
}
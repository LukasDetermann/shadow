package io.determann.shadow.api.dsl.enum_;

import io.determann.shadow.api.dsl.enum_constant.EnumConstantRenderable;

import java.util.Arrays;
import java.util.List;

public interface EnumEnumConstantStep
      extends EnumBodyStep
{
   EnumEnumConstantStep enumConstant(String... enumConstant);

   default EnumEnumConstantStep enumConstant(EnumConstantRenderable... enumConstant)
   {
      return enumConstant(Arrays.asList(enumConstant));
   }

   EnumEnumConstantStep enumConstant(List<? extends EnumConstantRenderable> enumConstant);
}
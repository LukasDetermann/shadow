package io.determann.shadow.api.dsl.enum_;

import io.determann.shadow.api.dsl.enum_constant.EnumConstantRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface EnumEnumConstantStep
      extends EnumBodyStep
{
   @Contract(value = "_ -> new", pure = true)
   EnumEnumConstantStep enumConstant(String... enumConstant);

   @Contract(value = "_ -> new", pure = true)
   default EnumEnumConstantStep enumConstant(EnumConstantRenderable... enumConstant)
   {
      return enumConstant(Arrays.asList(enumConstant));
   }

   @Contract(value = "_ -> new", pure = true)
   EnumEnumConstantStep enumConstant(List<? extends EnumConstantRenderable> enumConstant);
}
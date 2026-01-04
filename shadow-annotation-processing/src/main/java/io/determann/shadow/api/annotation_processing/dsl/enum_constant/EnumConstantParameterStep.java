package io.determann.shadow.api.annotation_processing.dsl.enum_constant;

import io.determann.shadow.api.annotation_processing.dsl.parameter.ParameterRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface EnumConstantParameterStep
      extends EnumConstantBodyStep
{
   @Contract(value = "_ -> new", pure = true)
   EnumConstantParameterStep parameter(String... parameter);

   @Contract(value = "_ -> new", pure = true)
   default EnumConstantParameterStep parameter(ParameterRenderable... parameter)
   {
      return parameter(Arrays.asList(parameter));
   }

   @Contract(value = "_ -> new", pure = true)
   EnumConstantParameterStep parameter(List<? extends ParameterRenderable> parameter);
}
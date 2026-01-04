package io.determann.shadow.api.annotation_processing.dsl.constructor;

import io.determann.shadow.api.annotation_processing.dsl.parameter.ParameterRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface ConstructorParameterStep
      extends ConstructorThrowsStep
{
   @Contract(value = "_ -> new", pure = true)
   ConstructorParameterStep parameter(String... parameter);

   @Contract(value = "_ -> new", pure = true)
   default ConstructorParameterStep parameter(ParameterRenderable... parameter)
   {
      return parameter(Arrays.asList(parameter));
   }

   @Contract(value = "_ -> new", pure = true)
   ConstructorParameterStep parameter(List<? extends ParameterRenderable> parameter);
}

package io.determann.shadow.api.dsl.method;

import io.determann.shadow.api.dsl.parameter.ParameterRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface MethodParameterStep
      extends MethodThrowsStep
{
   @Contract(value = "_ -> new", pure = true)
   MethodParameterStep parameter(String... parameter);

   @Contract(value = "_ -> new", pure = true)
   default MethodParameterStep parameter(ParameterRenderable... parameter)
   {
      return parameter(Arrays.asList(parameter));
   }

   @Contract(value = "_ -> new", pure = true)
   MethodParameterStep parameter(List<? extends ParameterRenderable> parameter);
}

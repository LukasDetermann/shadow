package io.determann.shadow.api.dsl.method;

import io.determann.shadow.api.dsl.generic.GenericRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface MethodGenericStep
      extends MethodResultStep
{
   @Contract(value = "_ -> new", pure = true)
   MethodGenericStep generic(String... generic);

   @Contract(value = "_ -> new", pure = true)
   default MethodGenericStep generic(GenericRenderable... generic)
   {
      return generic(Arrays.asList(generic));
   }

   @Contract(value = "_ -> new", pure = true)
   MethodGenericStep generic(List<? extends GenericRenderable> generic);
}
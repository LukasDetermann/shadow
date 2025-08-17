package io.determann.shadow.api.dsl.constructor;

import io.determann.shadow.api.dsl.generic.GenericRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface ConstructorGenericStep
      extends ConstructorTypeStep
{
   @Contract(value = "_ -> new", pure = true)
   ConstructorGenericStep generic(String... generic);

   @Contract(value = "_ -> new", pure = true)
   default ConstructorGenericStep generic(GenericRenderable... generic)
   {
      return generic(Arrays.asList(generic));
   }

   @Contract(value = "_ -> new", pure = true)
   ConstructorGenericStep generic(List<? extends GenericRenderable> generic);
}

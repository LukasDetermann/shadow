package io.determann.shadow.api.dsl.interface_;

import io.determann.shadow.api.dsl.generic.GenericRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface InterfaceGenericStep
      extends InterfaceExtendsStep
{
   @Contract(value = "_ -> new", pure = true)
   InterfaceGenericStep generic(String... generics);

   @Contract(value = "_ -> new", pure = true)
   default InterfaceGenericStep generic(GenericRenderable... generics)
   {
      return generic(Arrays.asList(generics));
   }

   @Contract(value = "_ -> new", pure = true)
   InterfaceGenericStep generic(List<? extends GenericRenderable> generics);
}

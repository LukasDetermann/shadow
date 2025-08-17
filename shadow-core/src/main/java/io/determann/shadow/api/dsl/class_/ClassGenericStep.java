package io.determann.shadow.api.dsl.class_;

import io.determann.shadow.api.dsl.generic.GenericRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface ClassGenericStep
      extends ClassExtendsStep
{
   @Contract(value = "_ -> new", pure = true)
   ClassGenericStep generic(String... generics);

   @Contract(value = "_ -> new", pure = true)
   default ClassGenericStep generic(GenericRenderable... generics)
   {
      return generic(Arrays.asList(generics));
   }

   @Contract(value = "_ -> new", pure = true)
   ClassGenericStep generic(List<? extends GenericRenderable> generics);
}

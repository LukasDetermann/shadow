package io.determann.shadow.api.dsl.constructor;

import io.determann.shadow.api.dsl.class_.ClassRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface ConstructorThrowsStep
      extends ConstructorBodyStep
{
   @Contract(value = "_ -> new", pure = true)
   ConstructorThrowsStep throws_(String... exception);

   @Contract(value = "_ -> new", pure = true)
   default ConstructorThrowsStep throws_(ClassRenderable... exception)
   {
      return throws_(Arrays.asList(exception));
   }

   @Contract(value = "_ -> new", pure = true)
   ConstructorThrowsStep throws_(List<? extends ClassRenderable> exception);
}

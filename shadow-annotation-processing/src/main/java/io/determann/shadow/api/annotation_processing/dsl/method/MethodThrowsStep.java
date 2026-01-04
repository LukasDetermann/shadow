package io.determann.shadow.api.annotation_processing.dsl.method;

import io.determann.shadow.api.annotation_processing.dsl.class_.ClassRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface MethodThrowsStep
      extends MethodBodyStep
{
   @Contract(value = "_ -> new", pure = true)
   MethodThrowsStep throws_(String... exception);

   @Contract(value = "_ -> new", pure = true)
   default MethodThrowsStep throws_(ClassRenderable... exception)
   {
      return throws_(Arrays.asList(exception));
   }

   @Contract(value = "_ -> new", pure = true)
   MethodThrowsStep throws_(List<? extends ClassRenderable> exception);
}

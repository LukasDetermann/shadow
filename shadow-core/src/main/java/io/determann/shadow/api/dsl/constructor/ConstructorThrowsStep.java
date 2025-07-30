package io.determann.shadow.api.dsl.constructor;

import io.determann.shadow.api.dsl.class_.ClassRenderable;

import java.util.Arrays;
import java.util.List;

public interface ConstructorThrowsStep extends ConstructorBodyStep
{
   ConstructorThrowsStep throws_(String... exception);

   default ConstructorThrowsStep throws_(ClassRenderable... exception)
   {
      return throws_(Arrays.asList(exception));
   }

   ConstructorThrowsStep throws_(List<? extends ClassRenderable> exception);
}

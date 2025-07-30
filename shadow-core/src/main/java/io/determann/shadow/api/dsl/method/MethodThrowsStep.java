package io.determann.shadow.api.dsl.method;

import io.determann.shadow.api.dsl.class_.ClassRenderable;

import java.util.Arrays;
import java.util.List;

public interface MethodThrowsStep extends MethodBodyStep
{
   MethodThrowsStep throws_(String... exception);

   default MethodThrowsStep throws_(ClassRenderable... exception)
   {
      return throws_(Arrays.asList(exception));
   }

   MethodThrowsStep throws_(List<? extends ClassRenderable> exception);
}

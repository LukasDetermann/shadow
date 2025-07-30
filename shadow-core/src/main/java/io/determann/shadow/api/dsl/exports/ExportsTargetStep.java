package io.determann.shadow.api.dsl.exports;

import io.determann.shadow.api.dsl.module.ModuleNameRenderable;

import java.util.Arrays;
import java.util.List;

public interface ExportsTargetStep
      extends ExportsRenderable
{
   ExportsTargetStep to(String... moduleNames);

   default ExportsTargetStep to(ModuleNameRenderable... modules)
   {
      return to(Arrays.asList(modules));
   }

   ExportsTargetStep to(List<? extends ModuleNameRenderable> modules);
}

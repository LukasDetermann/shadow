package io.determann.shadow.api.dsl.opens;

import io.determann.shadow.api.dsl.module.ModuleNameRenderable;

import java.util.Arrays;
import java.util.List;

public interface OpensTargetStep
      extends OpensRenderable
{
   OpensTargetStep to(String... moduleNames);

   default OpensTargetStep to(ModuleNameRenderable... modules)
   {
      return to(Arrays.asList(modules));
   }

   OpensTargetStep to(List<? extends ModuleNameRenderable> modules);
}

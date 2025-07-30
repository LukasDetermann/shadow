package io.determann.shadow.api.dsl.module;

import io.determann.shadow.api.dsl.opens.OpensRenderable;

import java.util.Arrays;
import java.util.List;

public interface ModuleOpensStep extends ModuleUsesStep
{
   ModuleOpensStep opens(String... opens);

   default ModuleOpensStep opens(OpensRenderable... opens)
   {
      return opens(Arrays.asList(opens));
   }

   ModuleOpensStep opens(List<? extends OpensRenderable> opens);
}

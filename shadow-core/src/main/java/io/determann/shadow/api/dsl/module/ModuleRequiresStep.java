package io.determann.shadow.api.dsl.module;

import io.determann.shadow.api.dsl.requires.RequiresRenderable;

import java.util.Arrays;
import java.util.List;

public interface ModuleRequiresStep extends ModuleExportsStep
{
   ModuleRequiresStep requires(String... requires);

   default ModuleRequiresStep requires(RequiresRenderable... requires)
   {
      return requires(Arrays.asList(requires));
   }

   ModuleRequiresStep requires(List<? extends RequiresRenderable> requires);
}

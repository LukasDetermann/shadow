package io.determann.shadow.api.dsl.module;

import io.determann.shadow.api.dsl.uses.UsesRenderable;

import java.util.Arrays;
import java.util.List;

public interface ModuleUsesStep extends ModuleProvidesStep
{
   ModuleUsesStep uses(String... uses);

   default ModuleUsesStep uses(UsesRenderable... uses)
   {
      return uses(Arrays.asList(uses));
   }

   ModuleUsesStep uses(List<? extends UsesRenderable> uses);
}
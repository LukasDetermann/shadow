package io.determann.shadow.api.dsl.module;

import io.determann.shadow.api.dsl.requires.RequiresRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface ModuleRequiresStep
      extends ModuleExportsStep
{
   @Contract(value = "_ -> new", pure = true)
   ModuleRequiresStep requires(String... requires);

   @Contract(value = "_ -> new", pure = true)
   default ModuleRequiresStep requires(RequiresRenderable... requires)
   {
      return requires(Arrays.asList(requires));
   }

   @Contract(value = "_ -> new", pure = true)
   ModuleRequiresStep requires(List<? extends RequiresRenderable> requires);
}

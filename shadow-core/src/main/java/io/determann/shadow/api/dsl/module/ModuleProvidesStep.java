package io.determann.shadow.api.dsl.module;

import io.determann.shadow.api.dsl.provides.ProvidesRenderable;

import java.util.Arrays;
import java.util.List;

public interface ModuleProvidesStep
      extends ModuleInfoRenderable,
              ModuleNameRenderable
{
   ModuleProvidesStep provides(String... provides);

   default ModuleProvidesStep provides(ProvidesRenderable... provides)
   {
      return provides(Arrays.asList(provides));
   }

   ModuleProvidesStep provides(List<? extends ProvidesRenderable> provides);
}
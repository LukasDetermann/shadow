package io.determann.shadow.api.dsl.module;

import io.determann.shadow.api.dsl.provides.ProvidesRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface ModuleProvidesStep
      extends ModuleRenderable
{
   @Contract(value = "_ -> new", pure = true)
   ModuleProvidesStep provides(String... provides);

   @Contract(value = "_ -> new", pure = true)
   default ModuleProvidesStep provides(ProvidesRenderable... provides)
   {
      return provides(Arrays.asList(provides));
   }

   @Contract(value = "_ -> new", pure = true)
   ModuleProvidesStep provides(List<? extends ProvidesRenderable> provides);
}
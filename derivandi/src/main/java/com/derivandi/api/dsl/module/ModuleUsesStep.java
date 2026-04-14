package com.derivandi.api.dsl.module;

import com.derivandi.api.dsl.uses.UsesRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface ModuleUsesStep
      extends ModuleProvidesStep
{
   @Contract(value = "_ -> new", pure = true)
   ModuleUsesStep uses(String... uses);

   @Contract(value = "_ -> new", pure = true)
   default ModuleUsesStep uses(UsesRenderable... uses)
   {
      return uses(Arrays.asList(uses));
   }

   @Contract(value = "_ -> new", pure = true)
   ModuleUsesStep uses(List<? extends UsesRenderable> uses);
}
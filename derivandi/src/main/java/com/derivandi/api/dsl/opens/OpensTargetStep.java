package com.derivandi.api.dsl.opens;

import com.derivandi.api.dsl.module.ModuleRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface OpensTargetStep
      extends OpensRenderable
{
   @Contract(value = "_ -> new", pure = true)
   OpensTargetStep to(String... moduleNames);

   @Contract(value = "_ -> new", pure = true)
   default OpensTargetStep to(ModuleRenderable... modules)
   {
      return to(Arrays.asList(modules));
   }

   @Contract(value = "_ -> new", pure = true)
   OpensTargetStep to(List<? extends ModuleRenderable> modules);
}

package io.determann.shadow.api.dsl.opens;

import io.determann.shadow.api.dsl.module.ModuleNameRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface OpensTargetStep
      extends OpensRenderable
{
   @Contract(value = "_ -> new", pure = true)
   OpensTargetStep to(String... moduleNames);

   @Contract(value = "_ -> new", pure = true)
   default OpensTargetStep to(ModuleNameRenderable... modules)
   {
      return to(Arrays.asList(modules));
   }

   @Contract(value = "_ -> new", pure = true)
   OpensTargetStep to(List<? extends ModuleNameRenderable> modules);
}

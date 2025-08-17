package io.determann.shadow.api.dsl.exports;

import io.determann.shadow.api.dsl.module.ModuleNameRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface ExportsTargetStep
      extends ExportsRenderable
{
   @Contract(value = "_ -> new", pure = true)
   ExportsTargetStep to(String... moduleNames);

   @Contract(value = "_ -> new", pure = true)
   default ExportsTargetStep to(ModuleNameRenderable... modules)
   {
      return to(Arrays.asList(modules));
   }

   @Contract(value = "_ -> new", pure = true)
   ExportsTargetStep to(List<? extends ModuleNameRenderable> modules);
}

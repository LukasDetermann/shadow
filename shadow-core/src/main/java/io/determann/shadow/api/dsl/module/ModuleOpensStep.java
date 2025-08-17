package io.determann.shadow.api.dsl.module;

import io.determann.shadow.api.dsl.opens.OpensRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface ModuleOpensStep
      extends ModuleUsesStep
{
   @Contract(value = "_ -> new", pure = true)
   ModuleOpensStep opens(String... opens);

   @Contract(value = "_ -> new", pure = true)
   default ModuleOpensStep opens(OpensRenderable... opens)
   {
      return opens(Arrays.asList(opens));
   }

   @Contract(value = "_ -> new", pure = true)
   ModuleOpensStep opens(List<? extends OpensRenderable> opens);
}

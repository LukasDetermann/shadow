package io.determann.shadow.api.dsl.module;

import io.determann.shadow.api.dsl.exports.ExportsRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface ModuleExportsStep
      extends ModuleOpensStep
{
   @Contract(value = "_ -> new", pure = true)
   ModuleExportsStep exports(String... exports);

   @Contract(value = "_ -> new", pure = true)
   default ModuleExportsStep exports(ExportsRenderable... exports)
   {
      return exports(Arrays.asList(exports));
   }

   @Contract(value = "_ -> new", pure = true)
   ModuleExportsStep exports(List<? extends ExportsRenderable> exports);
}

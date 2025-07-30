package io.determann.shadow.api.dsl.module;

import io.determann.shadow.api.dsl.exports.ExportsRenderable;

import java.util.Arrays;
import java.util.List;

public interface ModuleExportsStep extends ModuleOpensStep
{
   ModuleExportsStep exports(String... exports);

   default ModuleExportsStep exports(ExportsRenderable... exports)
   {
      return exports(Arrays.asList(exports));
   }

   ModuleExportsStep exports(List<? extends ExportsRenderable> exports);
}

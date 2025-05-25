package io.determann.shadow.api.dsl.module;

import io.determann.shadow.api.dsl.exports.ExportsRenderable;
import io.determann.shadow.api.shadow.directive.C_Exports;

public interface ModuleExportsStep extends ModuleOpensStep
{
   ModuleExportsStep exports(String exports);

   ModuleExportsStep exports(C_Exports exports);

   ModuleExportsStep exports(ExportsRenderable exports);
}

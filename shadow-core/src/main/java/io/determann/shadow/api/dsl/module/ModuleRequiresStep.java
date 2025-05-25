package io.determann.shadow.api.dsl.module;

import io.determann.shadow.api.dsl.requires.RequiresRenderable;
import io.determann.shadow.api.shadow.directive.C_Requires;

public interface ModuleRequiresStep extends ModuleExportsStep
{
   ModuleRequiresStep requires(String requires);

   ModuleRequiresStep requires(C_Requires requires);

   ModuleRequiresStep requires(RequiresRenderable requires);
}

package io.determann.shadow.api.dsl.module;

import io.determann.shadow.api.dsl.uses.UsesRenderable;
import io.determann.shadow.api.shadow.directive.C_Uses;

public interface ModuleUsesStep extends ModuleProvidesStep
{
   ModuleUsesStep uses(String uses);

   ModuleUsesStep uses(C_Uses uses);

   ModuleUsesStep uses(UsesRenderable uses);
}

package io.determann.shadow.api.dsl.module;

import io.determann.shadow.api.shadow.directive.C_Opens;

public interface ModuleOpensStep extends ModuleUsesStep
{
   ModuleOpensStep opens(String opens);

   ModuleOpensStep opens(C_Opens opens);
}

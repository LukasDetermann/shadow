package io.determann.shadow.api.dsl.module;

import io.determann.shadow.api.dsl.provides.ProvidesRenderable;
import io.determann.shadow.api.shadow.directive.C_Provides;

public interface ModuleProvidesStep extends ModuleRenderable
{
   ModuleProvidesStep provides(String provides);

   ModuleProvidesStep provides(C_Provides provides);

   ModuleProvidesStep provides(ProvidesRenderable provides);
}

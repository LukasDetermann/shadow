package io.determann.shadow.api.dsl.requires;

import io.determann.shadow.api.shadow.structure.C_Module;

public interface RequiresNameStep
{
   RequiresRenderable dependency(String moduleName);

   RequiresRenderable dependency(C_Module module);
}

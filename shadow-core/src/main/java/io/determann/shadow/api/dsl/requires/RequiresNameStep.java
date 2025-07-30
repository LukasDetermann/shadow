package io.determann.shadow.api.dsl.requires;

import io.determann.shadow.api.dsl.module.ModuleNameRenderable;

public interface RequiresNameStep
{
   RequiresRenderable dependency(String moduleName);

   RequiresRenderable dependency(ModuleNameRenderable module);
}

package io.determann.shadow.api.dsl.requires;

import io.determann.shadow.api.dsl.module.ModuleNameRenderable;
import org.jetbrains.annotations.Contract;

public interface RequiresNameStep
{
   @Contract(value = "_ -> new", pure = true)
   RequiresRenderable dependency(String moduleName);

   @Contract(value = "_ -> new", pure = true)
   RequiresRenderable dependency(ModuleNameRenderable module);
}

package io.determann.shadow.api.annotation_processing.dsl.requires;

import io.determann.shadow.api.annotation_processing.dsl.module.ModuleRenderable;
import org.jetbrains.annotations.Contract;

public interface RequiresNameStep
{
   @Contract(value = "_ -> new", pure = true)
   RequiresRenderable dependency(String moduleName);

   @Contract(value = "_ -> new", pure = true)
   RequiresRenderable dependency(ModuleRenderable module);
}

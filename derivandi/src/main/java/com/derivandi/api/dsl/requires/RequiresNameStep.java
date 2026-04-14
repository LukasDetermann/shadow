package com.derivandi.api.dsl.requires;

import com.derivandi.api.dsl.module.ModuleRenderable;
import org.jetbrains.annotations.Contract;

public interface RequiresNameStep
{
   @Contract(value = "_ -> new", pure = true)
   RequiresRenderable dependency(String moduleName);

   @Contract(value = "_ -> new", pure = true)
   RequiresRenderable dependency(ModuleRenderable module);
}

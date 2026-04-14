package com.derivandi.api.dsl.module;

import com.derivandi.api.dsl.RenderingContext;
import org.jetbrains.annotations.Contract;

public interface ModuleRenderable
{
   @Contract(value = "_ -> new", pure = true)
   String renderModuleInfo(RenderingContext renderingContext);

   @Contract(value = "_ -> new", pure = true)
   String renderQualifiedName(RenderingContext renderingContext);
}

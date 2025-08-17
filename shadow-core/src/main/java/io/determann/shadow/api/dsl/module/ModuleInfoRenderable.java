package io.determann.shadow.api.dsl.module;

import io.determann.shadow.api.dsl.RenderingContext;
import org.jetbrains.annotations.Contract;

public interface ModuleInfoRenderable
{
   @Contract(value = "_ -> new", pure = true)
   String renderModuleInfo(RenderingContext renderingContext);
}

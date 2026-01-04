package io.determann.shadow.api.annotation_processing.dsl.module;

import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;
import org.jetbrains.annotations.Contract;

public interface ModuleRenderable
{
   @Contract(value = "_ -> new", pure = true)
   String renderModuleInfo(RenderingContext renderingContext);

   @Contract(value = "_ -> new", pure = true)
   String renderQualifiedName(RenderingContext renderingContext);
}

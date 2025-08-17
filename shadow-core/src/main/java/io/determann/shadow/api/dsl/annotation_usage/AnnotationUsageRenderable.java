package io.determann.shadow.api.dsl.annotation_usage;

import io.determann.shadow.api.renderer.RenderingContext;
import org.jetbrains.annotations.Contract;

public interface AnnotationUsageRenderable
{
   @Contract(value = "_ -> new", pure = true)
   String renderDeclaration(RenderingContext renderingContext);
}

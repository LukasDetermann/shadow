package io.determann.shadow.api.dsl.annotation_value;

import io.determann.shadow.api.renderer.RenderingContext;
import org.jetbrains.annotations.Contract;

public interface AnnotationValueRenderable
{
   @Contract(value = "_ -> new", pure = true)
   String render(RenderingContext renderingContext);
}

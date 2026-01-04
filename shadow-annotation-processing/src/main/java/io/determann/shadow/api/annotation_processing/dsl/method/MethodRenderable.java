package io.determann.shadow.api.annotation_processing.dsl.method;

import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;
import org.jetbrains.annotations.Contract;

public interface MethodRenderable
{
   @Contract(value = "_ -> new", pure = true)
   String renderDeclaration(RenderingContext renderingContext);

   @Contract(value = "_ -> new", pure = true)
   String renderName(RenderingContext renderingContext);
}

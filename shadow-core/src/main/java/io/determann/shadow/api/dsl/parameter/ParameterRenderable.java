package io.determann.shadow.api.dsl.parameter;

import io.determann.shadow.api.renderer.RenderingContext;
import org.jetbrains.annotations.Contract;

public interface ParameterRenderable
{
   @Contract(value = "_ -> new", pure = true)
   String renderDeclaration(RenderingContext renderingContext);

   @Contract(value = "_ -> new", pure = true)
   String renderName(RenderingContext renderingContext);
}

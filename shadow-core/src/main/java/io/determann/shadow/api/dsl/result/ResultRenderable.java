package io.determann.shadow.api.dsl.result;

import io.determann.shadow.api.renderer.RenderingContext;
import org.jetbrains.annotations.Contract;

public interface ResultRenderable
{
   @Contract(value = "_ -> new", pure = true)
   String renderDeclaration(RenderingContext renderingContext);
}

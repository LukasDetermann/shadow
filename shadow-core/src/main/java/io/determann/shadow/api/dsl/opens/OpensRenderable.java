package io.determann.shadow.api.dsl.opens;

import io.determann.shadow.api.renderer.RenderingContext;
import org.jetbrains.annotations.Contract;

public interface OpensRenderable
{
   @Contract(value = "_ -> new", pure = true)
   String renderDeclaration(RenderingContext renderingContext);
}

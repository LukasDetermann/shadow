package io.determann.shadow.api.dsl;

import io.determann.shadow.api.renderer.RenderingContext;
import org.jetbrains.annotations.Contract;

public interface TypeRenderable
{
   /// renders a qualified name if possible, otherwise a simple name
   @Contract(value = "_ -> new", pure = true)
   String renderName(RenderingContext renderingContext);
}

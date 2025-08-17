package io.determann.shadow.api.dsl;

import org.jetbrains.annotations.Contract;

public interface TypeRenderable
{
   /// renders a qualified name if possible, otherwise a simple name
   @Contract(value = "_ -> new", pure = true)
   String renderName(RenderingContext renderingContext);
}

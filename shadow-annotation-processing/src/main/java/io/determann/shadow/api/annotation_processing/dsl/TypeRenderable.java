package io.determann.shadow.api.annotation_processing.dsl;

import org.jetbrains.annotations.Contract;

public interface TypeRenderable
{
   /// renders a qualified name if possible, otherwise a simple name
   @Contract(value = "_ -> new", pure = true)
   String renderName(RenderingContext renderingContext);

   /// renders a qualified name if possible, otherwise a simple name with genricUsages
   @Contract(value = "_ -> new", pure = true)
   String renderType(RenderingContext renderingContext);
}

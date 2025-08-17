package io.determann.shadow.api.dsl;

import org.jetbrains.annotations.Contract;

public interface VariableTypeRenderable
      extends TypeRenderable
{
   /// renders a qualified name if possible, otherwise a simple name
   @Contract(value = "_ -> new", pure = true)
   String renderType(RenderingContext renderingContext);
}

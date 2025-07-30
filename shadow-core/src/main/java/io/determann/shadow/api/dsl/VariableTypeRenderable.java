package io.determann.shadow.api.dsl;

import io.determann.shadow.api.renderer.RenderingContext;

public interface VariableTypeRenderable
      extends TypeRenderable
{
   /// renders a qualified name if possible, otherwise a simple name
   String renderType(RenderingContext renderingContext);
}

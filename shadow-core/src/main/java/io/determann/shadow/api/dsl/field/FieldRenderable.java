package io.determann.shadow.api.dsl.field;

import io.determann.shadow.api.renderer.RenderingContext;

public interface FieldRenderable
{
   String renderDeclaration(RenderingContext renderingContext);
}

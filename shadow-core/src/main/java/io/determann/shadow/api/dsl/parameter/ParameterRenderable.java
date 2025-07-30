package io.determann.shadow.api.dsl.parameter;

import io.determann.shadow.api.renderer.RenderingContext;

public interface ParameterRenderable
{
   String renderDeclaration(RenderingContext renderingContext);
}

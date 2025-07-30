package io.determann.shadow.api.dsl.generic;

import io.determann.shadow.api.dsl.ReferenceTypeRenderable;
import io.determann.shadow.api.renderer.RenderingContext;

public interface GenericRenderable extends ReferenceTypeRenderable
{
   String renderDeclaration(RenderingContext renderingContext);
}

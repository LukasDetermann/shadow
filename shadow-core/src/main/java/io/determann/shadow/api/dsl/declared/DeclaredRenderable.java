package io.determann.shadow.api.dsl.declared;

import io.determann.shadow.api.dsl.ReferenceTypeRenderable;
import io.determann.shadow.api.renderer.RenderingContext;

public interface DeclaredRenderable
      extends ReferenceTypeRenderable
{
   String renderDeclaration(RenderingContext renderingContext);

   String renderQualifiedName(RenderingContext renderingContext);
}

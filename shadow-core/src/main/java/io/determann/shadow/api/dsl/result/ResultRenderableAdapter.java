package io.determann.shadow.api.dsl.result;

import io.determann.shadow.api.dsl.RenderingContext;

import static io.determann.shadow.internal.dsl.AdapterSupport.notImplemented;

/// convenience for creating renderable
public abstract class ResultRenderableAdapter implements ResultRenderable
{
   @Override
   public String renderDeclaration(RenderingContext renderingContext)
   {
      throw notImplemented();
   }
}

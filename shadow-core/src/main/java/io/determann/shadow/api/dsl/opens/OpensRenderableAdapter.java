package io.determann.shadow.api.dsl.opens;

import io.determann.shadow.api.dsl.RenderingContext;

import static io.determann.shadow.internal.dsl.AdapterSupport.notImplemented;

/// convenience for creating renderable
public abstract class OpensRenderableAdapter implements OpensRenderable
{
   @Override
   public String renderDeclaration(RenderingContext renderingContext)
   {
      throw notImplemented();
   }
}

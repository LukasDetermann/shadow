package io.determann.shadow.api.dsl.receiver;

import io.determann.shadow.api.dsl.RenderingContext;

import static io.determann.shadow.internal.dsl.AdapterSupport.notImplemented;

/// convenience for creating renderable
public abstract class ReceiverRenderableAdapter implements ReceiverRenderable
{
   @Override
   public String renderDeclaration(RenderingContext renderingContext)
   {
      throw notImplemented();
   }
}

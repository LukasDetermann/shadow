package io.determann.shadow.api.annotation_processing.dsl.requires;

import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;

import static io.determann.shadow.internal.annotation_processing.dsl.AdapterSupport.notImplemented;

/// convenience for creating renderable
public abstract class RequiresRenderableAdapter implements RequiresRenderable
{
   @Override
   public String renderDeclaration(RenderingContext renderingContext)
   {
      throw notImplemented();
   }
}

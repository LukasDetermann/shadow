package io.determann.shadow.api.dsl.annotation_usage;

import io.determann.shadow.api.dsl.RenderingContext;

import static io.determann.shadow.internal.dsl.AdapterSupport.notImplemented;

/// convenience for creating renderable
public abstract class AnnotationUsageRenderableAdapter
      implements AnnotationUsageRenderable
{
   @Override
   public String renderDeclaration(RenderingContext renderingContext)
   {
      throw notImplemented();
   }
}

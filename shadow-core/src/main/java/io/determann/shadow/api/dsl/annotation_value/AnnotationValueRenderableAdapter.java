package io.determann.shadow.api.dsl.annotation_value;

import io.determann.shadow.api.dsl.RenderingContext;

import static io.determann.shadow.internal.dsl.AdapterSupport.notImplemented;

/// convenience for creating renderable
public abstract class AnnotationValueRenderableAdapter implements AnnotationValueRenderable
{
   @Override
   public String render(RenderingContext renderingContext)
   {
      throw notImplemented();
   }
}

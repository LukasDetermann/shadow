package io.determann.shadow.api.annotation_processing.dsl.annotation_value;

import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;

import static io.determann.shadow.internal.annotation_processing.dsl.AdapterSupport.notImplemented;

/// convenience for creating renderable
public abstract class AnnotationValueRenderableAdapter implements AnnotationValueRenderable
{
   @Override
   public String render(RenderingContext renderingContext)
   {
      throw notImplemented();
   }
}

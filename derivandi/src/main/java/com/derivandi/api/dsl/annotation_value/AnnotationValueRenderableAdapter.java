package com.derivandi.api.dsl.annotation_value;

import com.derivandi.api.dsl.RenderingContext;

import static com.derivandi.internal.dsl.AdapterSupport.notImplemented;

/// convenience for creating renderable
public abstract class AnnotationValueRenderableAdapter implements AnnotationValueRenderable
{
   @Override
   public String render(RenderingContext renderingContext)
   {
      throw notImplemented();
   }
}

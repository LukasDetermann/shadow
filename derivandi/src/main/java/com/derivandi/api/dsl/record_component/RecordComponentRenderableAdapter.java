package com.derivandi.api.dsl.record_component;

import com.derivandi.api.dsl.RenderingContext;

import static com.derivandi.internal.dsl.AdapterSupport.notImplemented;

/// convenience for creating renderable
public abstract class RecordComponentRenderableAdapter implements RecordComponentRenderable
{
   @Override
   public String renderDeclaration(RenderingContext renderingContext)
   {
      throw notImplemented();
   }
}

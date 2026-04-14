package com.derivandi.api.dsl.provides;

import com.derivandi.api.dsl.RenderingContext;

import static com.derivandi.internal.dsl.AdapterSupport.notImplemented;

/// convenience for creating renderable
public abstract class ProvidesRenderableAdapter implements ProvidesRenderable
{
   @Override
   public String renderDeclaration(RenderingContext renderingContext)
   {
      throw notImplemented();
   }
}

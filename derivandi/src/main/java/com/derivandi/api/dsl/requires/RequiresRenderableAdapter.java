package com.derivandi.api.dsl.requires;

import com.derivandi.api.dsl.RenderingContext;

import static com.derivandi.internal.dsl.AdapterSupport.notImplemented;

/// convenience for creating renderable
public abstract class RequiresRenderableAdapter implements RequiresRenderable
{
   @Override
   public String renderDeclaration(RenderingContext renderingContext)
   {
      throw notImplemented();
   }
}

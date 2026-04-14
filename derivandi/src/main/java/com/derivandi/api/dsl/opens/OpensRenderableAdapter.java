package com.derivandi.api.dsl.opens;

import com.derivandi.api.dsl.RenderingContext;

import static com.derivandi.internal.dsl.AdapterSupport.notImplemented;

/// convenience for creating renderable
public abstract class OpensRenderableAdapter implements OpensRenderable
{
   @Override
   public String renderDeclaration(RenderingContext renderingContext)
   {
      throw notImplemented();
   }
}

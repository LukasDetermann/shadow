package com.derivandi.api.dsl.result;

import com.derivandi.api.dsl.RenderingContext;

import static com.derivandi.internal.dsl.AdapterSupport.notImplemented;

/// convenience for creating renderable
public abstract class ResultRenderableAdapter implements ResultRenderable
{
   @Override
   public String renderDeclaration(RenderingContext renderingContext)
   {
      throw notImplemented();
   }
}

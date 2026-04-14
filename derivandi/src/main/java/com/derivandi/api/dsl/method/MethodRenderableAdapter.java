package com.derivandi.api.dsl.method;

import com.derivandi.api.dsl.RenderingContext;

import static com.derivandi.internal.dsl.AdapterSupport.notImplemented;

/// convenience for creating renderable
public abstract class MethodRenderableAdapter implements MethodRenderable
{
   @Override
   public String renderDeclaration(RenderingContext renderingContext)
   {
      throw notImplemented();
   }

   @Override
   public String renderName(RenderingContext renderingContext)
   {
      throw notImplemented();
   }
}

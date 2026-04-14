package com.derivandi.api.dsl.generic;

import com.derivandi.api.dsl.RenderingContext;

import static com.derivandi.internal.dsl.AdapterSupport.notImplemented;

/// convenience for creating renderable
public abstract class GenericRenderableAdapter implements GenericRenderable
{
   @Override
   public String renderDeclaration(RenderingContext renderingContext)
   {
      throw notImplemented();
   }

   @Override
   public String renderType(RenderingContext renderingContext)
   {
      throw notImplemented();
   }

   @Override
   public String renderName(RenderingContext renderingContext)
   {
      throw notImplemented();
   }
}

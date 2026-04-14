package com.derivandi.api.dsl.array;

import com.derivandi.api.dsl.RenderingContext;

import static com.derivandi.internal.dsl.AdapterSupport.notImplemented;

/// convenience for creating renderable
public abstract class ArrayRenderableAdapter implements ArrayRenderable
{
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
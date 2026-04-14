package com.derivandi.api.dsl.class_;

import com.derivandi.api.dsl.RenderingContext;

import static com.derivandi.internal.dsl.AdapterSupport.notImplemented;

/// convenience for creating renderable
public abstract class ClassRenderableAdapter implements ClassRenderable
{
   @Override
   public String renderDeclaration(RenderingContext renderingContext)
   {
      throw notImplemented();
   }

   @Override
   public String renderQualifiedName(RenderingContext renderingContext)
   {
      throw notImplemented();
   }

   @Override
   public String renderSimpleName(RenderingContext renderingContext)
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
package io.determann.shadow.api.annotation_processing.dsl.class_;

import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;

import static io.determann.shadow.internal.annotation_processing.dsl.AdapterSupport.notImplemented;

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
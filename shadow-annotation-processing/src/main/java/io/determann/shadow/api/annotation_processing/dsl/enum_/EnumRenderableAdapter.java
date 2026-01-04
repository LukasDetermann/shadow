package io.determann.shadow.api.annotation_processing.dsl.enum_;

import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;
import org.jetbrains.annotations.Contract;

import static io.determann.shadow.internal.annotation_processing.dsl.AdapterSupport.notImplemented;

/// convenience for creating renderable
public abstract class EnumRenderableAdapter implements EnumRenderable
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

   @Contract(value = "_ -> new", pure = true)
   @Override
   public String renderType(RenderingContext renderingContext)
   {
      throw notImplemented();
   }

   @Contract(value = "_ -> new", pure = true)
   @Override
   public String renderName(RenderingContext renderingContext)
   {
      throw notImplemented();
   }
}

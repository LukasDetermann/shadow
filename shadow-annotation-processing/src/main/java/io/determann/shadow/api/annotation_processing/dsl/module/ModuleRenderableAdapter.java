package io.determann.shadow.api.annotation_processing.dsl.module;

import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;

import static io.determann.shadow.internal.annotation_processing.dsl.AdapterSupport.notImplemented;

/// convenience for creating renderable
public abstract class ModuleRenderableAdapter implements ModuleRenderable
{
   @Override
   public String renderModuleInfo(RenderingContext renderingContext)
   {
      throw notImplemented();
   }

   @Override
   public String renderQualifiedName(RenderingContext renderingContext)
   {
      throw notImplemented();
   }
}

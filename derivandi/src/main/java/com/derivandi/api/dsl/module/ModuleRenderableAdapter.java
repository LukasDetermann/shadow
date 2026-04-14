package com.derivandi.api.dsl.module;

import com.derivandi.api.dsl.RenderingContext;

import static com.derivandi.internal.dsl.AdapterSupport.notImplemented;

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

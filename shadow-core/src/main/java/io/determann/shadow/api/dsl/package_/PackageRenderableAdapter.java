package io.determann.shadow.api.dsl.package_;

import io.determann.shadow.api.dsl.RenderingContext;

import static io.determann.shadow.internal.dsl.AdapterSupport.notImplemented;

/// convenience for creating renderable
public abstract class PackageRenderableAdapter implements PackageRenderable
{
   @Override
   public String renderQualifiedName(RenderingContext renderingContext)
   {
      throw notImplemented();
   }

   @Override
   public String renderPackageInfo(RenderingContext renderingContext)
   {
      throw notImplemented();
   }
}

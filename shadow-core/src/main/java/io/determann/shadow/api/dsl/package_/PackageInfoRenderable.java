package io.determann.shadow.api.dsl.package_;

import io.determann.shadow.api.renderer.RenderingContext;

public interface PackageInfoRenderable extends PackageRenderable
{
   String renderPackageInfo(RenderingContext renderingContext);
}

package io.determann.shadow.api.dsl.package_;

import io.determann.shadow.api.renderer.RenderingContext;

public interface PackageRenderable
{
   default String renderDeclaration(RenderingContext renderingContext)
   {
      return "package " + renderQualifiedName(renderingContext) + ';';
   }

   String renderQualifiedName(RenderingContext renderingContext);
}

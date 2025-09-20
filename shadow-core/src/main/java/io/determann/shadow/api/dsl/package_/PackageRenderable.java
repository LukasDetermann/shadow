package io.determann.shadow.api.dsl.package_;

import io.determann.shadow.api.dsl.RenderingContext;
import org.jetbrains.annotations.Contract;

public interface PackageRenderable
{
   @Contract(value = "_ -> new", pure = true)
   default String renderDeclaration(RenderingContext renderingContext)
   {
      return "package " + renderQualifiedName(renderingContext) + ';';
   }

   @Contract(value = "_ -> new", pure = true)
   String renderQualifiedName(RenderingContext renderingContext);

   @Contract(value = "_ -> new", pure = true)
   String renderPackageInfo(RenderingContext renderingContext);
}

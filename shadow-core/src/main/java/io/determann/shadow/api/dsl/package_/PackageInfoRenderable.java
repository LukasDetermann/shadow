package io.determann.shadow.api.dsl.package_;

import io.determann.shadow.api.dsl.RenderingContext;
import org.jetbrains.annotations.Contract;

public interface PackageInfoRenderable
      extends PackageRenderable
{
   @Contract(value = "_ -> new", pure = true)
   String renderPackageInfo(RenderingContext renderingContext);
}

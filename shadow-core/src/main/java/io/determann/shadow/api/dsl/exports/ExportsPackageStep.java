package io.determann.shadow.api.dsl.exports;

import io.determann.shadow.api.dsl.package_.PackageRenderable;
import org.jetbrains.annotations.Contract;

public interface ExportsPackageStep
{
   @Contract(value = "_ -> new", pure = true)
   ExportsTargetStep package_(String packageName);

   @Contract(value = "_ -> new", pure = true)
   ExportsTargetStep package_(PackageRenderable aPackage);
}

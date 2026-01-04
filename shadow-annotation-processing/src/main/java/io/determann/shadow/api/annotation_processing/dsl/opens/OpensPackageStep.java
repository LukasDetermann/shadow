package io.determann.shadow.api.annotation_processing.dsl.opens;

import io.determann.shadow.api.annotation_processing.dsl.package_.PackageRenderable;
import org.jetbrains.annotations.Contract;

public interface OpensPackageStep
{
   @Contract(value = "_ -> new", pure = true)
   OpensTargetStep package_(String packageName);

   @Contract(value = "_ -> new", pure = true)
   OpensTargetStep package_(PackageRenderable aPackage);
}

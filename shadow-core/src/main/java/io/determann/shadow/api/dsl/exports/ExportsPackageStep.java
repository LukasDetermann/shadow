package io.determann.shadow.api.dsl.exports;

import io.determann.shadow.api.dsl.package_.PackageRenderable;

public interface ExportsPackageStep
{
   ExportsTargetStep package_(String packageName);

   ExportsTargetStep package_(PackageRenderable aPackage);
}

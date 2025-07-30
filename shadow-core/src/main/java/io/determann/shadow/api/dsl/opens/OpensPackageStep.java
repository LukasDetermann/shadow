package io.determann.shadow.api.dsl.opens;

import io.determann.shadow.api.dsl.package_.PackageRenderable;

public interface OpensPackageStep
{
   OpensTargetStep package_(String packageName);

   OpensTargetStep package_(PackageRenderable aPackage);
}

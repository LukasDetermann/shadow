package io.determann.shadow.api.dsl.interface_;

import io.determann.shadow.api.dsl.package_.PackageRenderable;

public interface InterfacePackageStep
{
   InterfaceImportStep package_(String packageName);

   InterfaceImportStep package_(PackageRenderable aPackage);
}

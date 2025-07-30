package io.determann.shadow.api.dsl.enum_;

import io.determann.shadow.api.dsl.package_.PackageRenderable;

public interface EnumPackageStep
{
   EnumImportStep package_(String packageName);

   EnumImportStep package_(PackageRenderable aPackage);
}

package io.determann.shadow.api.dsl.class_;

import io.determann.shadow.api.dsl.package_.PackageRenderable;

public interface ClassPackageStep
{
   ClassImportStep package_(String packageName);

   ClassImportStep package_(PackageRenderable aPackage);
}

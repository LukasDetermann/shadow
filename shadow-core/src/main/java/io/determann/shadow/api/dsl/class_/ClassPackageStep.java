package io.determann.shadow.api.dsl.class_;

import io.determann.shadow.api.shadow.structure.C_Package;

public interface ClassPackageStep
{
   ClassImportStep package_(String packageName);

   ClassImportStep package_(C_Package aPackage);
}

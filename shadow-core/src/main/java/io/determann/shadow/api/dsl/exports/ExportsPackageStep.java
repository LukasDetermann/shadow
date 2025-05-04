package io.determann.shadow.api.dsl.exports;

import io.determann.shadow.api.shadow.structure.C_Package;

public interface ExportsPackageStep
{
   ExportsTargetStep package_(String packageName);

   ExportsTargetStep package_(C_Package aPackage);
}

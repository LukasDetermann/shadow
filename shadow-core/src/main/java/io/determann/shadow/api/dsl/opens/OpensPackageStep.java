package io.determann.shadow.api.dsl.opens;

import io.determann.shadow.api.shadow.structure.C_Package;

public interface OpensPackageStep
{
   OpensTargetStep package_(String packageName);

   OpensTargetStep package_(C_Package aPackage);
}

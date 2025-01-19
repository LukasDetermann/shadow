package io.determann.shadow.api.dsl;

import io.determann.shadow.api.shadow.structure.C_Package;

public interface PackageStep extends ImportStep
{
   ImportStep package_(String name);

   ImportStep package_(C_Package aPackage);
}

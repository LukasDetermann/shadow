package io.determann.shadow.api.dsl.record;

import io.determann.shadow.api.dsl.package_.PackageRenderable;

public interface RecordPackageStep
{
   RecordImportStep package_(String packageName);

   RecordImportStep package_(PackageRenderable aPackage);
}

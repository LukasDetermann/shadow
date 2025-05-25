package io.determann.shadow.api.dsl.annotation;

import io.determann.shadow.api.shadow.structure.C_Package;

public interface AnnotationPackageStep
{
   AnnotationImportStep package_(String packageName);

   AnnotationImportStep package_(C_Package aPackage);
}

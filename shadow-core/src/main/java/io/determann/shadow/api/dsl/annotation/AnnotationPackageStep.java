package io.determann.shadow.api.dsl.annotation;

import io.determann.shadow.api.dsl.package_.PackageRenderable;

public interface AnnotationPackageStep
{
   AnnotationImportStep package_(String packageName);

   AnnotationImportStep package_(PackageRenderable aPackage);
}

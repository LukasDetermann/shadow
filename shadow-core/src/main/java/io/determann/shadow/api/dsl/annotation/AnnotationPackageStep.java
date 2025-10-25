package io.determann.shadow.api.dsl.annotation;

import io.determann.shadow.api.dsl.package_.PackageRenderable;
import org.jetbrains.annotations.Contract;

public interface AnnotationPackageStep
{
   @Contract(value = "_ -> new", pure = true)
   AnnotationImportStep package_(String packageName);

   @Contract(value = "_ -> new", pure = true)
   AnnotationImportStep package_(PackageRenderable aPackage);

   @Contract(value = "-> new", pure = true)
   AnnotationImportStep noPackage();
}

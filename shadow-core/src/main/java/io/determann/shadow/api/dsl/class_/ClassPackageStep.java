package io.determann.shadow.api.dsl.class_;

import io.determann.shadow.api.dsl.package_.PackageRenderable;
import org.jetbrains.annotations.Contract;

public interface ClassPackageStep
{
   @Contract(value = "_ -> new", pure = true)
   ClassImportStep package_(String packageName);

   @Contract(value = "_ -> new", pure = true)
   ClassImportStep package_(PackageRenderable aPackage);
}

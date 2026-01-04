package io.determann.shadow.api.annotation_processing.dsl.interface_;

import io.determann.shadow.api.annotation_processing.dsl.package_.PackageRenderable;
import org.jetbrains.annotations.Contract;

public interface InterfacePackageStep
{
   @Contract(value = "_ -> new", pure = true)
   InterfaceImportStep package_(String packageName);

   @Contract(value = "_ -> new", pure = true)
   InterfaceImportStep package_(PackageRenderable aPackage);

   @Contract(value = "-> new", pure = true)
   InterfaceImportStep noPackage();
}
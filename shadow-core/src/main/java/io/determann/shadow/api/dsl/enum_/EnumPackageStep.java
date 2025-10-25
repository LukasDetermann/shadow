package io.determann.shadow.api.dsl.enum_;

import io.determann.shadow.api.dsl.package_.PackageRenderable;
import org.jetbrains.annotations.Contract;

public interface EnumPackageStep
{
   @Contract(value = "_ -> new", pure = true)
   EnumImportStep package_(String packageName);

   @Contract(value = "_ -> new", pure = true)
   EnumImportStep package_(PackageRenderable aPackage);

   @Contract(value = "-> new", pure = true)
   EnumImportStep noPackage();
}
package io.determann.shadow.api.annotation_processing.dsl.record;

import io.determann.shadow.api.annotation_processing.dsl.package_.PackageRenderable;
import org.jetbrains.annotations.Contract;

public interface RecordPackageStep
{
   @Contract(value = "_ -> new", pure = true)
   RecordImportStep package_(String packageName);

   @Contract(value = "_ -> new", pure = true)
   RecordImportStep package_(PackageRenderable aPackage);

   @Contract(value = "-> new", pure = true)
   RecordImportStep noPackage();
}
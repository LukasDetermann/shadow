package io.determann.shadow.api.annotation_processing.dsl.import_;

import org.jetbrains.annotations.Contract;

public interface ImportStaticStep
      extends ImportTypeStep
{
   @Contract(value = " -> new", pure = true)
   StaticImportTypeStep static_();
}
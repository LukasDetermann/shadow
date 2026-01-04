package io.determann.shadow.api.annotation_processing.dsl.field;

import org.jetbrains.annotations.Contract;

public interface FieldAdditionalNameStep
      extends FieldRenderable
{
   @Contract(value = "_ -> new", pure = true)
   FieldAdditionalInitializationStep name(String string);
}

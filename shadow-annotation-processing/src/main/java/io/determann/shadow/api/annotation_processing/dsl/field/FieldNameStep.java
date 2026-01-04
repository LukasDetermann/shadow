package io.determann.shadow.api.annotation_processing.dsl.field;

import org.jetbrains.annotations.Contract;

public interface FieldNameStep
{
   @Contract(value = "_ -> new", pure = true)
   FieldInitializationStep name(String name);
}

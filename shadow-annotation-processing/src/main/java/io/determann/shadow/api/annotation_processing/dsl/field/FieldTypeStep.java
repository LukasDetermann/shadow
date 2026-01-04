package io.determann.shadow.api.annotation_processing.dsl.field;

import io.determann.shadow.api.annotation_processing.dsl.VariableTypeRenderable;
import org.jetbrains.annotations.Contract;

public interface FieldTypeStep
{
   @Contract(value = "_ -> new", pure = true)
   FieldNameStep type(String type);

   @Contract(value = "_ -> new", pure = true)
   FieldNameStep type(VariableTypeRenderable type);
}

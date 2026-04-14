package com.derivandi.api.dsl.field;

import com.derivandi.api.dsl.VariableTypeRenderable;
import org.jetbrains.annotations.Contract;

public interface FieldTypeStep
{
   @Contract(value = "_ -> new", pure = true)
   FieldNameStep type(String type);

   @Contract(value = "_ -> new", pure = true)
   FieldNameStep type(VariableTypeRenderable type);
}

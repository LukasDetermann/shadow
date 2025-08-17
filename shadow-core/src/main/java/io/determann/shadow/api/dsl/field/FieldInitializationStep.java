package io.determann.shadow.api.dsl.field;

import org.jetbrains.annotations.Contract;

public interface FieldInitializationStep
      extends FieldRenderable
{
   @Contract(value = "_ -> new", pure = true)
   FieldAdditionalNameStep initializer(String initializer);
}

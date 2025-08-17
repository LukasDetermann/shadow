package io.determann.shadow.api.dsl.field;

import org.jetbrains.annotations.Contract;

public interface FieldAdditionalInitializationStep
      extends FieldRenderable
{
   @Contract(value = "_ -> new", pure = true)
   FieldAdditionalNameStep initializer(String initializer);
}

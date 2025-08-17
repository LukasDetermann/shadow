package io.determann.shadow.api.dsl.enum_constant;

import org.jetbrains.annotations.Contract;

public interface EnumConstantBodyStep
      extends EnumConstantRenderable
{
   @Contract(value = "_ -> new", pure = true)
   EnumConstantRenderable body(String body);
}

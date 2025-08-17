package io.determann.shadow.api.dsl.enum_;

import org.jetbrains.annotations.Contract;

public interface EnumNameStep
{
   @Contract(value = "_ -> new", pure = true)
   EnumImplementsStep name(String name);
}

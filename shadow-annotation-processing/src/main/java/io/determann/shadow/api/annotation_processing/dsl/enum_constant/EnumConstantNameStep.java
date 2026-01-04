package io.determann.shadow.api.annotation_processing.dsl.enum_constant;

import org.jetbrains.annotations.Contract;

public interface EnumConstantNameStep
{
   @Contract(value = "_ -> new", pure = true)
   EnumConstantParameterStep name(String name);
}

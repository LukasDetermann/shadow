package io.determann.shadow.api.dsl.generic;

import org.jetbrains.annotations.Contract;

public interface GenericNameStep
{
   @Contract(value = "_ -> new", pure = true)
   GenericExtendsStep name(String name);
}

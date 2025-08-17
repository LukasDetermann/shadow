package io.determann.shadow.api.dsl.interface_;

import org.jetbrains.annotations.Contract;

public interface InterfaceNameStep
{
   @Contract(value = "_ -> new", pure = true)
   InterfaceGenericStep name(String name);
}

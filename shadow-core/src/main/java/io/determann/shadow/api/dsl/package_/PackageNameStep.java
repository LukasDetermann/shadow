package io.determann.shadow.api.dsl.package_;

import org.jetbrains.annotations.Contract;

public interface PackageNameStep
{
   @Contract(value = "_ -> new", pure = true)
   PackageRenderable name(String name);
}

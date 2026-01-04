package io.determann.shadow.api.annotation_processing.dsl.module;

import org.jetbrains.annotations.Contract;

public interface ModuleNameStep
{
   @Contract(value = "_ -> new", pure = true)
   ModuleRequiresStep name(String name);
}

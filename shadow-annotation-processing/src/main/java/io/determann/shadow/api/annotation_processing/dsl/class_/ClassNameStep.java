package io.determann.shadow.api.annotation_processing.dsl.class_;

import org.jetbrains.annotations.Contract;

public interface ClassNameStep
{
   @Contract(value = "_ -> new", pure = true)
   ClassGenericStep name(String name);
}

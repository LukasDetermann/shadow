package io.determann.shadow.api.dsl.record_component;

import org.jetbrains.annotations.Contract;

public interface RecordComponentNameStep
{
   @Contract(value = "_ -> new", pure = true)
   RecordComponentRenderable name(String name);
}

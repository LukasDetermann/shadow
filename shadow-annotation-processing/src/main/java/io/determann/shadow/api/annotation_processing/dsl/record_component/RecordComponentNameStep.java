package io.determann.shadow.api.annotation_processing.dsl.record_component;

import org.jetbrains.annotations.Contract;

public interface RecordComponentNameStep
{
   @Contract(value = "_ -> new", pure = true)
   RecordComponentRenderable name(String name);
}

package io.determann.shadow.api.annotation_processing.dsl.record_component;

import io.determann.shadow.api.annotation_processing.dsl.TypeRenderable;
import org.jetbrains.annotations.Contract;

public interface RecordComponentTypeStep
{
   @Contract(value = "_ -> new", pure = true)
   RecordComponentNameStep type(String type);

   @Contract(value = "_ -> new", pure = true)
   RecordComponentNameStep type(TypeRenderable type);
}

package io.determann.shadow.api.annotation_processing.dsl.annotation_usage;

import io.determann.shadow.api.annotation_processing.dsl.annotation_value.AnnotationValueRenderable;
import org.jetbrains.annotations.Contract;

public interface AnnotationUsageValueStep
{
   @Contract(value = "_ -> new", pure = true)
   AnnotationUsageNameStep value(String annotationValue);

   @Contract(value = "_ -> new", pure = true)
   AnnotationUsageNameStep value(AnnotationValueRenderable annotationValue);
}

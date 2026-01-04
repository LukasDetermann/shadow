package io.determann.shadow.api.annotation_processing.dsl.annotation_usage;

import io.determann.shadow.api.annotation_processing.dsl.annotation.AnnotationRenderable;
import org.jetbrains.annotations.Contract;

public interface AnnotationUsageTypeStep
{
   @Contract(value = "_ -> new", pure = true)
   AnnotationUsageNameStep type(String annotation);

   @Contract(value = "_ -> new", pure = true)
   AnnotationUsageNameStep type(AnnotationRenderable annotation);
}

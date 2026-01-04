package io.determann.shadow.api.annotation_processing.dsl.annotation;

import org.jetbrains.annotations.Contract;

public interface AnnotationNameStep
{
   @Contract(value = "_ -> new", pure = true)
   AnnotationBodyStep name(String name);
}

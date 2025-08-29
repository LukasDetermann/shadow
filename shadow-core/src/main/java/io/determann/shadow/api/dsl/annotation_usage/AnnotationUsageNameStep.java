package io.determann.shadow.api.dsl.annotation_usage;

import org.jetbrains.annotations.Contract;

public interface AnnotationUsageNameStep
      extends AnnotationUsageRenderable
{
   /// the name "value" can be omitted
   AnnotationUsageValueStep noName();

   @Contract(value = "_ -> new", pure = true)
   AnnotationUsageValueStep name(String name);
}

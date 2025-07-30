package io.determann.shadow.api.dsl.annotation_usage;

public interface AnnotationUsageNameStep
      extends AnnotationUsageRenderable
{
   AnnotationUsageValueStep noName();

   AnnotationUsageValueStep name(String name);
}

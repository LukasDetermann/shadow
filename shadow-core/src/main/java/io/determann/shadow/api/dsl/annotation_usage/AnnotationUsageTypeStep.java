package io.determann.shadow.api.dsl.annotation_usage;

import io.determann.shadow.api.dsl.annotation.AnnotationRenderable;

public interface AnnotationUsageTypeStep
{
   AnnotationUsageNameStep type(String annotation);

   AnnotationUsageNameStep type(AnnotationRenderable annotation);
}

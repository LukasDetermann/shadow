package io.determann.shadow.api.dsl.annotation_usage;

import io.determann.shadow.api.dsl.annotation_value.AnnotationValueRenderable;

public interface AnnotationUsageValueStep
{
   AnnotationUsageNameStep value(String annotationValue);

   AnnotationUsageNameStep value(AnnotationValueRenderable annotationValue);
}

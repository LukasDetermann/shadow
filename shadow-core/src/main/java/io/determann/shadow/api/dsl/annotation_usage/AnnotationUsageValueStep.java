package io.determann.shadow.api.dsl.annotation_usage;

import io.determann.shadow.api.dsl.annotation_value.AnnotationValueRenderable;
import io.determann.shadow.api.shadow.C_AnnotationValue;

public interface AnnotationUsageValueStep
{
   AnnotationUsageNameStep value(String annotationValue);

   AnnotationUsageNameStep value(C_AnnotationValue annotationValue);

   AnnotationUsageNameStep value(AnnotationValueRenderable annotationValue);
}

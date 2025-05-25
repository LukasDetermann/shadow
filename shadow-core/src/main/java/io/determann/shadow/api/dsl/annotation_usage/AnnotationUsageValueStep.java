package io.determann.shadow.api.dsl.annotation_usage;

import io.determann.shadow.api.shadow.C_AnnotationValue;

public interface AnnotationUsageValueStep
{
   AnnotationUsageNameStep value(C_AnnotationValue annotationValue);

   AnnotationUsageNameStep value(String annotationValue);
}

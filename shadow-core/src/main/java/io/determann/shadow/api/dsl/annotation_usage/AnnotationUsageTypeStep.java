package io.determann.shadow.api.dsl.annotation_usage;

import io.determann.shadow.api.shadow.type.C_Annotation;

public interface AnnotationUsageTypeStep
{
   AnnotationUsageNameStep type(String annotation);

   AnnotationUsageNameStep type(C_Annotation annotation);
}

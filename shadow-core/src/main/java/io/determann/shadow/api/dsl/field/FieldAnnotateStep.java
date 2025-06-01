package io.determann.shadow.api.dsl.field;

import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;
import io.determann.shadow.api.shadow.C_AnnotationUsage;

public interface FieldAnnotateStep extends FieldModifierStep
{
   FieldAnnotateStep annotate(String... annotation);

   FieldAnnotateStep annotate(C_AnnotationUsage... annotation);

   FieldAnnotateStep annotate(AnnotationUsageRenderable... annotation);
}

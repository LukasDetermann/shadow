package io.determann.shadow.api.dsl.annotation;

import io.determann.shadow.api.shadow.C_AnnotationUsage;

public interface AnnotationAnnotateStep
      extends AnnotationModifierStep
{
   AnnotationAnnotateStep annotate(String... annotation);

   AnnotationAnnotateStep annotate(C_AnnotationUsage... annotation);
}

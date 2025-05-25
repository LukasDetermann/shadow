package io.determann.shadow.api.dsl.annotation;

import io.determann.shadow.api.shadow.type.C_Annotation;

public interface AnnotationAnnotateStep
      extends AnnotationModifierStep
{
   AnnotationAnnotateStep annotate(String... annotation);

   AnnotationAnnotateStep annotate(C_Annotation... annotation);
}

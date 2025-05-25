package io.determann.shadow.api.dsl.constructor;

import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;
import io.determann.shadow.api.shadow.C_AnnotationUsage;

public interface ConstructorAnnotateStep
      extends ConstructorModifierStep
{
   ConstructorAnnotateStep annotate(String... annotation);

   ConstructorAnnotateStep annotate(C_AnnotationUsage... annotation);

   ConstructorAnnotateStep annotate(AnnotationUsageRenderable... annotation);
}

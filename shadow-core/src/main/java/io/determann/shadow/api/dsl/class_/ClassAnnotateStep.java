package io.determann.shadow.api.dsl.class_;

import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;
import io.determann.shadow.api.shadow.C_AnnotationUsage;

public interface ClassAnnotateStep
      extends ClassModifierStep
{
   ClassAnnotateStep annotate(String... annotation);

   ClassAnnotateStep annotate(C_AnnotationUsage... annotation);

   ClassAnnotateStep annotate(AnnotationUsageRenderable... annotation);
}

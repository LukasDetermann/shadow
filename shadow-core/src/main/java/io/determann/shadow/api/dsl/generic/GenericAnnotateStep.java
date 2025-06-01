package io.determann.shadow.api.dsl.generic;

import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;
import io.determann.shadow.api.shadow.C_AnnotationUsage;

public interface GenericAnnotateStep
      extends GenericNameStep
{
   GenericAnnotateStep annotate(String... annotation);

   GenericAnnotateStep annotate(C_AnnotationUsage... annotation);

   GenericAnnotateStep annotate(AnnotationUsageRenderable... annotation);
}

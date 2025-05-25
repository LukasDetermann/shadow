package io.determann.shadow.api.dsl.package_;

import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;
import io.determann.shadow.api.shadow.C_AnnotationUsage;

public interface PackageAnnotateStep
      extends PackageNameStep
{
   PackageAnnotateStep annotate(String... annotation);

   PackageAnnotateStep annotate(C_AnnotationUsage... annotation);

   PackageAnnotateStep annotate(AnnotationUsageRenderable... annotation);
}

package io.determann.shadow.api.dsl.package_;

import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;

import java.util.Arrays;
import java.util.List;

public interface PackageAnnotateStep
      extends PackageNameStep
{
   PackageAnnotateStep annotate(String... annotation);

   default PackageAnnotateStep annotate(AnnotationUsageRenderable... annotation)
   {
      return annotate(Arrays.asList(annotation));
   }

   PackageAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation);
}

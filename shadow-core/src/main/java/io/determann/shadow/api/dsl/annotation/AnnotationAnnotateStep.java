package io.determann.shadow.api.dsl.annotation;

import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;

import java.util.Arrays;
import java.util.List;

public interface AnnotationAnnotateStep
      extends AnnotationModifierStep
{
   AnnotationAnnotateStep annotate(String... annotation);

   default AnnotationAnnotateStep annotate(AnnotationUsageRenderable... annotation)
   {
      return annotate(Arrays.asList(annotation));
   }

   AnnotationAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation);
}
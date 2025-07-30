package io.determann.shadow.api.dsl.method;

import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;

import java.util.Arrays;
import java.util.List;

public interface MethodAnnotateStep
      extends MethodModifierStep
{
   MethodAnnotateStep annotate(String... annotation);

   default MethodAnnotateStep annotate(AnnotationUsageRenderable... annotation)
   {
      return annotate(Arrays.asList(annotation));
   }

   MethodAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation);
}
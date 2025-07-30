package io.determann.shadow.api.dsl.parameter;

import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;

import java.util.Arrays;
import java.util.List;

public interface ParameterAnnotateStep
      extends ParameterModifierStep
{
   ParameterAnnotateStep annotate(String... annotation);

   default ParameterAnnotateStep annotate(AnnotationUsageRenderable... annotation)
   {
      return annotate(Arrays.asList(annotation));
   }

   ParameterAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation);
}
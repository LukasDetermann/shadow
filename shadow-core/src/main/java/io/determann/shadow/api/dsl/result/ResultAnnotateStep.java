package io.determann.shadow.api.dsl.result;

import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;

import java.util.Arrays;
import java.util.List;

public interface ResultAnnotateStep
      extends ResultTypeStep
{
   ResultAnnotateStep annotate(String... annotation);

   default ResultAnnotateStep annotate(AnnotationUsageRenderable... annotation)
   {
      return annotate(Arrays.asList(annotation));
   }

   ResultAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation);
}
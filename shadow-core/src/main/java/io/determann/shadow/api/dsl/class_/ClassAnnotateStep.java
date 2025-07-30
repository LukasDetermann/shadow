package io.determann.shadow.api.dsl.class_;

import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;

import java.util.Arrays;
import java.util.List;

public interface ClassAnnotateStep
      extends ClassModifierStep
{
   ClassAnnotateStep annotate(String... annotation);

   default ClassAnnotateStep annotate(AnnotationUsageRenderable... annotation)
   {
      return annotate(Arrays.asList(annotation));
   }

   ClassAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation);
}
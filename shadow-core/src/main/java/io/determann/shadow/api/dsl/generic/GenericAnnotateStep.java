package io.determann.shadow.api.dsl.generic;

import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;

import java.util.Arrays;
import java.util.List;

public interface GenericAnnotateStep
      extends GenericNameStep
{
   GenericAnnotateStep annotate(String... annotation);

   default GenericAnnotateStep annotate(AnnotationUsageRenderable... annotation)
   {
      return annotate(Arrays.asList(annotation));
   }

   GenericAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation);
}
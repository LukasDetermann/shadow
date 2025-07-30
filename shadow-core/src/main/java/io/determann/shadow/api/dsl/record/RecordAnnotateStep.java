package io.determann.shadow.api.dsl.record;

import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;

import java.util.Arrays;
import java.util.List;

public interface RecordAnnotateStep
      extends RecordModifierStep
{
   RecordAnnotateStep annotate(String... annotation);

   default RecordAnnotateStep annotate(AnnotationUsageRenderable... annotation)
   {
      return annotate(Arrays.asList(annotation));
   }

   RecordAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation);
}
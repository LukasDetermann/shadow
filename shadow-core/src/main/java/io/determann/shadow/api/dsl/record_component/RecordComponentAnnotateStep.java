package io.determann.shadow.api.dsl.record_component;

import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;

import java.util.Arrays;
import java.util.List;

public interface RecordComponentAnnotateStep extends RecordComponentNameStep
{
   RecordComponentAnnotateStep annotate(String... annotation);

   default RecordComponentAnnotateStep annotate(AnnotationUsageRenderable... annotation)
   {
      return annotate(Arrays.asList(annotation));
   }

   RecordComponentAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation);
}
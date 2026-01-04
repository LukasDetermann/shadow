package io.determann.shadow.api.annotation_processing.dsl.record_component;

import io.determann.shadow.api.annotation_processing.dsl.annotation_usage.AnnotationUsageRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface RecordComponentAnnotateStep
      extends RecordComponentTypeStep
{
   @Contract(value = "_ -> new", pure = true)
   RecordComponentAnnotateStep annotate(String... annotation);

   @Contract(value = "_ -> new", pure = true)
   default RecordComponentAnnotateStep annotate(AnnotationUsageRenderable... annotation)
   {
      return annotate(Arrays.asList(annotation));
   }

   @Contract(value = "_ -> new", pure = true)
   RecordComponentAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation);
}
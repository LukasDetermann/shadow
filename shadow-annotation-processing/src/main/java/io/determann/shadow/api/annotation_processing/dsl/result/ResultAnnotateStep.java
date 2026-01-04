package io.determann.shadow.api.annotation_processing.dsl.result;

import io.determann.shadow.api.annotation_processing.dsl.annotation_usage.AnnotationUsageRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface ResultAnnotateStep
      extends ResultTypeStep
{
   @Contract(value = "_ -> new", pure = true)
   ResultAnnotateStep annotate(String... annotation);

   @Contract(value = "_ -> new", pure = true)
   default ResultAnnotateStep annotate(AnnotationUsageRenderable... annotation)
   {
      return annotate(Arrays.asList(annotation));
   }

   @Contract(value = "_ -> new", pure = true)
   ResultAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation);
}
package io.determann.shadow.api.annotation_processing.dsl.annotation;

import io.determann.shadow.api.annotation_processing.dsl.annotation_usage.AnnotationUsageRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface AnnotationAnnotateStep
      extends AnnotationModifierStep
{
   @Contract(value = "_ -> new", pure = true)
   AnnotationAnnotateStep annotate(String... annotation);

   @Contract(value = "_ -> new", pure = true)
   default AnnotationAnnotateStep annotate(AnnotationUsageRenderable... annotation)
   {
      return annotate(Arrays.asList(annotation));
   }

   @Contract(value = "_ -> new", pure = true)
   AnnotationAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation);
}
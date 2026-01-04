package io.determann.shadow.api.annotation_processing.dsl.field;

import io.determann.shadow.api.annotation_processing.dsl.annotation_usage.AnnotationUsageRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface FieldAnnotateStep
      extends FieldModifierStep
{
   @Contract(value = "_ -> new", pure = true)
   FieldAnnotateStep annotate(String... annotation);

   @Contract(value = "_ -> new", pure = true)
   default FieldAnnotateStep annotate(AnnotationUsageRenderable... annotation)
   {
      return annotate(Arrays.asList(annotation));
   }

   @Contract(value = "_ -> new", pure = true)
   FieldAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation);
}
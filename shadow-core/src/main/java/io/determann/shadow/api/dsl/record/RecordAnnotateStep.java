package io.determann.shadow.api.dsl.record;

import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface RecordAnnotateStep
      extends RecordModifierStep
{
   @Contract(value = "_ -> new", pure = true)
   RecordAnnotateStep annotate(String... annotation);

   @Contract(value = "_ -> new", pure = true)
   default RecordAnnotateStep annotate(AnnotationUsageRenderable... annotation)
   {
      return annotate(Arrays.asList(annotation));
   }

   @Contract(value = "_ -> new", pure = true)
   RecordAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation);
}
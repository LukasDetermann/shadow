package io.determann.shadow.api.dsl.class_;

import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface ClassAnnotateStep
      extends ClassModifierStep
{
   @Contract(value = "_ -> new", pure = true)
   ClassAnnotateStep annotate(String... annotation);

   @Contract(value = "_ -> new", pure = true)
   default ClassAnnotateStep annotate(AnnotationUsageRenderable... annotation)
   {
      return annotate(Arrays.asList(annotation));
   }

   @Contract(value = "_ -> new", pure = true)
   ClassAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation);
}
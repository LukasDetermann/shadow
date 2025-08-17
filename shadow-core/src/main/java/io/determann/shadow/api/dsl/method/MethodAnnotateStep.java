package io.determann.shadow.api.dsl.method;

import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface MethodAnnotateStep
      extends MethodModifierStep
{
   @Contract(value = "_ -> new", pure = true)
   MethodAnnotateStep annotate(String... annotation);

   @Contract(value = "_ -> new", pure = true)
   default MethodAnnotateStep annotate(AnnotationUsageRenderable... annotation)
   {
      return annotate(Arrays.asList(annotation));
   }

   @Contract(value = "_ -> new", pure = true)
   MethodAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation);
}
package io.determann.shadow.api.dsl.constructor;

import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface ConstructorAnnotateStep
      extends ConstructorModifierStep
{
   @Contract(value = "_ -> new", pure = true)
   ConstructorAnnotateStep annotate(String... annotation);

   @Contract(value = "_ -> new", pure = true)
   default ConstructorAnnotateStep annotate(AnnotationUsageRenderable... annotation)
   {
      return annotate(Arrays.asList(annotation));
   }

   @Contract(value = "_ -> new", pure = true)
   ConstructorAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation);
}
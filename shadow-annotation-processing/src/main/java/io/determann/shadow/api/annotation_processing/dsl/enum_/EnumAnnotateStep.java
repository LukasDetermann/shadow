package io.determann.shadow.api.annotation_processing.dsl.enum_;

import io.determann.shadow.api.annotation_processing.dsl.annotation_usage.AnnotationUsageRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface EnumAnnotateStep
      extends EnumModifierStep
{
   @Contract(value = "_ -> new", pure = true)
   EnumAnnotateStep annotate(String... annotation);

   @Contract(value = "_ -> new", pure = true)
   default EnumAnnotateStep annotate(AnnotationUsageRenderable... annotation)
   {
      return annotate(Arrays.asList(annotation));
   }

   @Contract(value = "_ -> new", pure = true)
   EnumAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation);
}
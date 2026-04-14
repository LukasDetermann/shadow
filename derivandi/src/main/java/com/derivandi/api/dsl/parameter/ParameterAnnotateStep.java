package com.derivandi.api.dsl.parameter;

import com.derivandi.api.dsl.annotation_usage.AnnotationUsageRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface ParameterAnnotateStep
      extends ParameterModifierStep
{
   @Contract(value = "_ -> new", pure = true)
   ParameterAnnotateStep annotate(String... annotation);

   @Contract(value = "_ -> new", pure = true)
   default ParameterAnnotateStep annotate(AnnotationUsageRenderable... annotation)
   {
      return annotate(Arrays.asList(annotation));
   }

   @Contract(value = "_ -> new", pure = true)
   ParameterAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation);
}
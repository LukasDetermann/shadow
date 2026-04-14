package com.derivandi.api.dsl.generic;

import com.derivandi.api.dsl.annotation_usage.AnnotationUsageRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface GenericAnnotateStep
      extends GenericNameStep
{
   @Contract(value = "_ -> new", pure = true)
   GenericAnnotateStep annotate(String... annotation);

   @Contract(value = "_ -> new", pure = true)
   default GenericAnnotateStep annotate(AnnotationUsageRenderable... annotation)
   {
      return annotate(Arrays.asList(annotation));
   }

   @Contract(value = "_ -> new", pure = true)
   GenericAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation);
}
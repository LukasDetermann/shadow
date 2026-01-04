package io.determann.shadow.api.annotation_processing.dsl.module;

import io.determann.shadow.api.annotation_processing.dsl.annotation_usage.AnnotationUsageRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface ModuleAnnotateStep
      extends ModuleNameStep
{
   @Contract(value = "_ -> new", pure = true)
   ModuleAnnotateStep annotate(String... annotation);

   @Contract(value = "_ -> new", pure = true)
   default ModuleAnnotateStep annotate(AnnotationUsageRenderable... annotation)
   {
      return annotate(Arrays.asList(annotation));
   }

   @Contract(value = "_ -> new", pure = true)
   ModuleAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation);
}
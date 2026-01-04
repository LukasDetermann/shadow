package io.determann.shadow.api.annotation_processing.dsl.package_;

import io.determann.shadow.api.annotation_processing.dsl.annotation_usage.AnnotationUsageRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface PackageAnnotateStep
      extends PackageNameStep
{
   @Contract(value = "_ -> new", pure = true)
   PackageAnnotateStep annotate(String... annotation);

   @Contract(value = "_ -> new", pure = true)
   default PackageAnnotateStep annotate(AnnotationUsageRenderable... annotation)
   {
      return annotate(Arrays.asList(annotation));
   }

   @Contract(value = "_ -> new", pure = true)
   PackageAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation);
}

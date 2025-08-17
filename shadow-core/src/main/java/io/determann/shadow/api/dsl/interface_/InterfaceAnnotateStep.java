package io.determann.shadow.api.dsl.interface_;

import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface InterfaceAnnotateStep
      extends InterfaceModifierStep
{
   @Contract(value = "_ -> new", pure = true)
   InterfaceAnnotateStep annotate(String... annotation);

   @Contract(value = "_ -> new", pure = true)
   default InterfaceAnnotateStep annotate(AnnotationUsageRenderable... annotation)
   {
      return annotate(Arrays.asList(annotation));
   }

   @Contract(value = "_ -> new", pure = true)
   InterfaceAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation);
}
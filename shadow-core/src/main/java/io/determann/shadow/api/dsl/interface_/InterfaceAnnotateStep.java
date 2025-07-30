package io.determann.shadow.api.dsl.interface_;

import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;

import java.util.Arrays;
import java.util.List;

public interface InterfaceAnnotateStep
      extends InterfaceModifierStep
{
   InterfaceAnnotateStep annotate(String... annotation);

   default InterfaceAnnotateStep annotate(AnnotationUsageRenderable... annotation)
   {
      return annotate(Arrays.asList(annotation));
   }

   InterfaceAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation);
}
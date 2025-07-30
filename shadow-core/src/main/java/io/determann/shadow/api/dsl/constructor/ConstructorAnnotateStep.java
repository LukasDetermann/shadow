package io.determann.shadow.api.dsl.constructor;

import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;

import java.util.Arrays;
import java.util.List;

public interface ConstructorAnnotateStep
      extends ConstructorModifierStep
{
   ConstructorAnnotateStep annotate(String... annotation);

   default ConstructorAnnotateStep annotate(AnnotationUsageRenderable... annotation)
   {
      return annotate(Arrays.asList(annotation));
   }

   ConstructorAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation);
}
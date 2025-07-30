package io.determann.shadow.api.dsl.field;

import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;

import java.util.Arrays;
import java.util.List;

public interface FieldAnnotateStep
      extends FieldModifierStep
{
   FieldAnnotateStep annotate(String... annotation);

   default FieldAnnotateStep annotate(AnnotationUsageRenderable... annotation)
   {
      return annotate(Arrays.asList(annotation));
   }

   FieldAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation);
}
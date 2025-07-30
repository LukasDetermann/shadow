package io.determann.shadow.api.dsl.enum_;

import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;

import java.util.Arrays;
import java.util.List;

public interface EnumAnnotateStep
      extends EnumModifierStep
{
   EnumAnnotateStep annotate(String... annotation);

   default EnumAnnotateStep annotate(AnnotationUsageRenderable... annotation)
   {
      return annotate(Arrays.asList(annotation));
   }

   EnumAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation);
}
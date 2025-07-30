package io.determann.shadow.api.dsl.enum_constant;

import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;

import java.util.Arrays;
import java.util.List;

public interface EnumConstantAnnotateStep extends EnumConstantNameStep
{
   EnumConstantAnnotateStep annotate(String... annotation);

   default EnumConstantAnnotateStep annotate(AnnotationUsageRenderable... annotation)
   {
      return annotate(Arrays.asList(annotation));
   }

   EnumConstantAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation);
}

package io.determann.shadow.api.dsl.module;

import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;

import java.util.Arrays;
import java.util.List;

public interface ModuleAnnotateStep
      extends ModuleNameStep
{
   ModuleAnnotateStep annotate(String... annotation);

   default ModuleAnnotateStep annotate(AnnotationUsageRenderable... annotation)
   {
      return annotate(Arrays.asList(annotation));
   }

   ModuleAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation);
}
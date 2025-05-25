package io.determann.shadow.api.dsl.module;

import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;
import io.determann.shadow.api.shadow.C_AnnotationUsage;

public interface ModuleAnnotateStep
      extends ModuleNameStep
{
   ModuleAnnotateStep annotate(String... annotation);

   ModuleAnnotateStep annotate(C_AnnotationUsage... annotation);

   ModuleAnnotateStep annotate(AnnotationUsageRenderable... annotation);
}

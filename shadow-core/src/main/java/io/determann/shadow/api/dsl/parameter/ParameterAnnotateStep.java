package io.determann.shadow.api.dsl.parameter;

import io.determann.shadow.api.shadow.C_AnnotationUsage;

public interface ParameterAnnotateStep
      extends ParameterModifierStep
{
   ParameterAnnotateStep annotate(String... annotation);

   ParameterAnnotateStep annotate(C_AnnotationUsage... annotation);
}

package io.determann.shadow.api.dsl.parameter;

import io.determann.shadow.api.shadow.type.C_Annotation;

public interface ParameterAnnotateStep
      extends ParameterModifierStep
{
   ParameterAnnotateStep annotate(String... annotation);

   ParameterAnnotateStep annotate(C_Annotation... annotation);
}

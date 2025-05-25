package io.determann.shadow.api.dsl.method;

import io.determann.shadow.api.shadow.C_AnnotationUsage;

public interface MethodAnnotateStep
      extends MethodModifierStep
{
   MethodAnnotateStep annotate(String... annotation);

   MethodAnnotateStep annotate(C_AnnotationUsage... annotation);
}

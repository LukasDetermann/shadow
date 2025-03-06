package io.determann.shadow.api.dsl.method;

import io.determann.shadow.api.shadow.type.C_Annotation;

public interface MethodAnnotateStep
      extends MethodModifierStep
{
   MethodAnnotateStep annotate(String... annotation);

   MethodAnnotateStep annotate(C_Annotation... annotation);
}

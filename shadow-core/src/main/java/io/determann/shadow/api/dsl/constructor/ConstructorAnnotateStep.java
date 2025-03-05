package io.determann.shadow.api.dsl.constructor;

import io.determann.shadow.api.shadow.type.C_Annotation;

public interface ConstructorAnnotateStep
      extends ConstructorModifierStep
{
   ConstructorAnnotateStep annotate(String... annotation);

   ConstructorAnnotateStep annotate(C_Annotation... annotation);
}

package io.determann.shadow.api.dsl.field;

import io.determann.shadow.api.shadow.type.C_Annotation;

public interface FieldAnnotateStep extends FieldModifierStep
{
   FieldModifierStep annotate(String... annotation);

   FieldModifierStep annotate(C_Annotation... annotation);
}

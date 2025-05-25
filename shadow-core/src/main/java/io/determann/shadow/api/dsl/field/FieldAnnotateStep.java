package io.determann.shadow.api.dsl.field;

import io.determann.shadow.api.shadow.C_AnnotationUsage;

public interface FieldAnnotateStep extends FieldModifierStep
{
   FieldModifierStep annotate(String... annotation);

   FieldModifierStep annotate(C_AnnotationUsage... annotation);
}

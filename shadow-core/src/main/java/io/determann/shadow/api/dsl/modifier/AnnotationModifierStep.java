package io.determann.shadow.api.dsl.modifier;

import io.determann.shadow.api.shadow.modifier.C_Modifier;

public interface AnnotationModifierStep
{
   AnnotationModifierStep modifier(String... modifiers);

   AnnotationModifierStep modifier(C_Modifier... modifiers);

   AnnotationModifierStep public_();

   AnnotationModifierStep protected_();

   AnnotationModifierStep private_();

   AnnotationModifierStep static_();

   AnnotationModifierStep strictfp_();
}

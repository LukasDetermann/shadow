package io.determann.shadow.api.dsl.annotation;

import io.determann.shadow.api.Modifier;

import java.util.Set;

public interface AnnotationModifierStep
      extends AnnotationNameStep
{
   AnnotationModifierStep modifier(String... modifiers);

   default AnnotationModifierStep modifier(Modifier... modifiers)
   {
      return modifier(Set.of(modifiers));
   }

   AnnotationModifierStep modifier(Set<Modifier> modifiers);

   AnnotationModifierStep public_();

   AnnotationModifierStep protected_();

   AnnotationModifierStep private_();

   AnnotationModifierStep abstract_();

   AnnotationModifierStep sealed();

   AnnotationModifierStep nonSealed();

   AnnotationModifierStep strictfp_();
}
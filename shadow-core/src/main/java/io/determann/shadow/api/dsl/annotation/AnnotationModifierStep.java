package io.determann.shadow.api.dsl.annotation;

import io.determann.shadow.api.Modifier;
import org.jetbrains.annotations.Contract;

import java.util.Set;

public interface AnnotationModifierStep
      extends AnnotationNameStep
{
   @Contract(value = "_ -> new", pure = true)
   AnnotationModifierStep modifier(String... modifiers);

   @Contract(value = "_ -> new", pure = true)
   default AnnotationModifierStep modifier(Modifier... modifiers)
   {
      return modifier(Set.of(modifiers));
   }

   @Contract(value = "_ -> new", pure = true)
   AnnotationModifierStep modifier(Set<Modifier> modifiers);

   @Contract(value = " -> new", pure = true)
   AnnotationModifierStep public_();

   @Contract(value = " -> new", pure = true)
   AnnotationModifierStep protected_();

   @Contract(value = " -> new", pure = true)
   AnnotationModifierStep private_();

   @Contract(value = " -> new", pure = true)
   AnnotationModifierStep abstract_();

   @Contract(value = " -> new", pure = true)
   AnnotationModifierStep sealed();

   @Contract(value = " -> new", pure = true)
   AnnotationModifierStep nonSealed();

   @Contract(value = " -> new", pure = true)
   AnnotationModifierStep strictfp_();
}
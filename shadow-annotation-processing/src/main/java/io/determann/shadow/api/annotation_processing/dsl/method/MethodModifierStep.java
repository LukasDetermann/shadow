package io.determann.shadow.api.annotation_processing.dsl.method;

import io.determann.shadow.api.annotation_processing.Modifier;
import org.jetbrains.annotations.Contract;

import java.util.Set;

public interface MethodModifierStep
      extends MethodGenericStep
{
   @Contract(value = "_ -> new", pure = true)
   MethodModifierStep modifier(String... modifiers);

   @Contract(value = "_ -> new", pure = true)
   default MethodModifierStep modifier(Modifier... modifiers)
   {
      return modifier(Set.of(modifiers));
   }

   @Contract(value = "_ -> new", pure = true)
   MethodModifierStep modifier(Set<Modifier> modifiers);

   @Contract(value = " -> new", pure = true)
   MethodModifierStep abstract_();

   @Contract(value = " -> new", pure = true)
   MethodModifierStep public_();

   @Contract(value = " -> new", pure = true)
   MethodModifierStep protected_();

   @Contract(value = " -> new", pure = true)
   MethodModifierStep private_();

   @Contract(value = " -> new", pure = true)
   MethodModifierStep default_();

   @Contract(value = " -> new", pure = true)
   MethodModifierStep final_();

   @Contract(value = " -> new", pure = true)
   MethodModifierStep native_();

   @Contract(value = " -> new", pure = true)
   MethodModifierStep static_();

   @Contract(value = " -> new", pure = true)
   MethodModifierStep strictfp_();
}
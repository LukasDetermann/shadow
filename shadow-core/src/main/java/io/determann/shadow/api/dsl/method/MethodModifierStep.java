package io.determann.shadow.api.dsl.method;

import io.determann.shadow.api.Modifier;

import java.util.Set;

public interface MethodModifierStep
      extends MethodGenericStep
{
   MethodModifierStep modifier(String... modifiers);

   default MethodModifierStep modifier(Modifier... modifiers)
   {
      return modifier(Set.of(modifiers));
   }

   MethodModifierStep modifier(Set<Modifier> modifiers);

   MethodModifierStep abstract_();

   MethodModifierStep public_();

   MethodModifierStep protected_();

   MethodModifierStep private_();

   MethodModifierStep default_();

   MethodModifierStep final_();

   MethodModifierStep native_();

   MethodModifierStep static_();

   MethodModifierStep strictfp_();
}
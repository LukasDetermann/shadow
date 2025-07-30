package io.determann.shadow.api.dsl.constructor;

import io.determann.shadow.api.shadow.modifier.C_Modifier;

import java.util.Set;

public interface ConstructorModifierStep
      extends ConstructorGenericStep
{
   ConstructorModifierStep modifier(String... modifiers);

   default ConstructorModifierStep modifier(C_Modifier... modifiers)
   {
      return modifier(Set.of(modifiers));
   }

   ConstructorModifierStep modifier(Set<C_Modifier> modifiers);

   ConstructorModifierStep public_();

   ConstructorModifierStep protected_();

   ConstructorModifierStep private_();
}

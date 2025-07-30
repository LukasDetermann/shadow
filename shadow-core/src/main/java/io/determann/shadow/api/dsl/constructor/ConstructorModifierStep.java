package io.determann.shadow.api.dsl.constructor;

import io.determann.shadow.api.Modifier;

import java.util.Set;

public interface ConstructorModifierStep
      extends ConstructorGenericStep
{
   ConstructorModifierStep modifier(String... modifiers);

   default ConstructorModifierStep modifier(Modifier... modifiers)
   {
      return modifier(Set.of(modifiers));
   }

   ConstructorModifierStep modifier(Set<Modifier> modifiers);

   ConstructorModifierStep public_();

   ConstructorModifierStep protected_();

   ConstructorModifierStep private_();
}

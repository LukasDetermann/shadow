package io.determann.shadow.api.dsl.constructor;

import io.determann.shadow.api.shadow.modifier.C_Modifier;

public interface ConstructorModifierStep extends ConstructorGenericStep
{
   ConstructorModifierStep modifier(String... modifiers);

   ConstructorModifierStep modifier(C_Modifier... modifiers);

   ConstructorModifierStep public_();

   ConstructorModifierStep protected_();

   ConstructorModifierStep private_();
}

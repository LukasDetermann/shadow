package io.determann.shadow.api.dsl.constructor;

import io.determann.shadow.api.shadow.modifier.C_Modifier;

public interface ConstructorModifierStep extends ConstructorGenericStep
{
   ConstructorGenericStep modifier(String... modifiers);

   ConstructorGenericStep modifier(C_Modifier... modifiers);

   ConstructorGenericStep public_();

   ConstructorGenericStep protected_();

   ConstructorGenericStep private_();
}

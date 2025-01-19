package io.determann.shadow.api.dsl;

import io.determann.shadow.api.shadow.modifier.C_Modifier;

public interface MethodModifierStep
{
   MethodModifierStep modifier(String... modifiers);

   MethodModifierStep modifier(C_Modifier... modifiers);

   MethodModifierStep default_();

   MethodModifierStep abstract_();

   MethodModifierStep public_();

   MethodModifierStep protected_();

   MethodModifierStep private_();

   MethodModifierStep final_();

   MethodModifierStep native_();
}

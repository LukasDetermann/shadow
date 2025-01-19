package io.determann.shadow.api.dsl.modifier;

import io.determann.shadow.api.shadow.modifier.C_Modifier;

public interface MethodModifierStep
{
   MethodModifierStep modifier(String... modifiers);

   MethodModifierStep modifier(C_Modifier... modifiers);

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

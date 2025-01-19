package io.determann.shadow.api.dsl.modifier;

import io.determann.shadow.api.shadow.modifier.C_Modifier;

public interface FieldModifierStep
{
   FieldModifierStep modifier(String... modifiers);

   FieldModifierStep modifier(C_Modifier... modifiers);

   FieldModifierStep public_();

   FieldModifierStep protected_();

   FieldModifierStep private_();

   FieldModifierStep final_();

   FieldModifierStep static_();
}

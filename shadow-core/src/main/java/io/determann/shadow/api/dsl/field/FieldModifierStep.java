package io.determann.shadow.api.dsl.field;

import io.determann.shadow.api.shadow.modifier.C_Modifier;

public interface FieldModifierStep extends FieldTypeStep
{
   FieldModifierStep modifier(String... modifiers);

   FieldModifierStep modifier(C_Modifier... modifiers);

   FieldModifierStep public_();

   FieldModifierStep protected_();

   FieldModifierStep private_();

   FieldModifierStep final_();

   FieldModifierStep static_();

   FieldModifierStep strictfp_();

   FieldModifierStep transient_();

   FieldModifierStep volatile_();
}

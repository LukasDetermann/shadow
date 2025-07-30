package io.determann.shadow.api.dsl.field;

import io.determann.shadow.api.shadow.modifier.C_Modifier;

import java.util.Set;

public interface FieldModifierStep
      extends FieldTypeStep
{
   FieldModifierStep modifier(String... modifiers);

   default FieldModifierStep modifier(C_Modifier... modifiers)
   {
      return modifier(Set.of(modifiers));
   }

   FieldModifierStep modifier(Set<C_Modifier> modifiers);

   FieldModifierStep public_();

   FieldModifierStep protected_();

   FieldModifierStep private_();

   FieldModifierStep final_();

   FieldModifierStep static_();

   FieldModifierStep strictfp_();

   FieldModifierStep transient_();

   FieldModifierStep volatile_();
}

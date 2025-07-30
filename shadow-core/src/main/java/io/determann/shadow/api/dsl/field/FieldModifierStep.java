package io.determann.shadow.api.dsl.field;

import io.determann.shadow.api.Modifier;

import java.util.Set;

public interface FieldModifierStep
      extends FieldTypeStep
{
   FieldModifierStep modifier(String... modifiers);

   default FieldModifierStep modifier(Modifier... modifiers)
   {
      return modifier(Set.of(modifiers));
   }

   FieldModifierStep modifier(Set<Modifier> modifiers);

   FieldModifierStep public_();

   FieldModifierStep protected_();

   FieldModifierStep private_();

   FieldModifierStep final_();

   FieldModifierStep static_();

   FieldModifierStep strictfp_();

   FieldModifierStep transient_();

   FieldModifierStep volatile_();
}

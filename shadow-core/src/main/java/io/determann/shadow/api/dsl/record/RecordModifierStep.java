package io.determann.shadow.api.dsl.record;

import io.determann.shadow.api.shadow.modifier.C_Modifier;

import java.util.Set;

public interface RecordModifierStep
      extends RecordNameStep
{
   RecordModifierStep modifier(String... modifiers);

   default RecordModifierStep modifier(C_Modifier... modifiers)
   {
      return modifier(Set.of(modifiers));
   }

   RecordModifierStep modifier(Set<C_Modifier> modifiers);

   RecordModifierStep public_();

   RecordModifierStep protected_();

   RecordModifierStep private_();

   RecordModifierStep final_();

   RecordModifierStep static_();

   RecordModifierStep strictfp_();
}

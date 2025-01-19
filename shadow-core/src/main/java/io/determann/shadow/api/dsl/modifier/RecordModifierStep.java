package io.determann.shadow.api.dsl.modifier;

import io.determann.shadow.api.shadow.modifier.C_Modifier;

public interface RecordModifierStep
{
   RecordModifierStep modifier(String... modifiers);

   RecordModifierStep modifier(C_Modifier... modifiers);

   RecordModifierStep public_();

   RecordModifierStep protected_();

   RecordModifierStep private_();

   RecordModifierStep final_();

   RecordModifierStep static_();

   RecordModifierStep strictfp_();
}

package io.determann.shadow.api.dsl.modifier;

import io.determann.shadow.api.dsl.ClassNameStep;
import io.determann.shadow.api.shadow.modifier.C_Modifier;

public interface ClassModifierStep extends ClassNameStep
{
   ClassModifierStep modifier(String... modifiers);

   ClassModifierStep modifier(C_Modifier... modifiers);

   ClassModifierStep abstract_();

   ClassModifierStep public_();

   ClassModifierStep protected_();

   ClassModifierStep private_();

   ClassModifierStep final_();

   ClassModifierStep sealed();

   ClassModifierStep nonSealed();

   ClassModifierStep static_();

   ClassModifierStep strictfp_();
}

package io.determann.shadow.api.dsl.modifier;

import io.determann.shadow.api.shadow.modifier.C_Modifier;

public interface EnumModifierStep
{
   EnumModifierStep modifier(String... modifiers);

   EnumModifierStep modifier(C_Modifier... modifiers);

   EnumModifierStep public_();

   EnumModifierStep protected_();

   EnumModifierStep private_();

   EnumModifierStep static_();

   EnumModifierStep strictfp_();
}

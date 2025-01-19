package io.determann.shadow.api.dsl.modifier;

import io.determann.shadow.api.shadow.modifier.C_Modifier;

public interface InterfaceModifierStep
{
   InterfaceModifierStep modifier(String... modifiers);

   InterfaceModifierStep modifier(C_Modifier... modifiers);

   InterfaceModifierStep abstract_();

   InterfaceModifierStep public_();

   InterfaceModifierStep protected_();

   InterfaceModifierStep private_();

   InterfaceModifierStep sealed();

   InterfaceModifierStep nonSealed();

   InterfaceModifierStep static_();

   InterfaceModifierStep strictfp_();
}

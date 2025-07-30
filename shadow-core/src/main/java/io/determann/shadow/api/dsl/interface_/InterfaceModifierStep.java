package io.determann.shadow.api.dsl.interface_;

import io.determann.shadow.api.shadow.modifier.C_Modifier;

import java.util.Set;

public interface InterfaceModifierStep
      extends InterfaceNameStep
{
   InterfaceModifierStep modifier(String... modifiers);

   default InterfaceModifierStep modifier(C_Modifier... modifiers)
   {
      return modifier(Set.of(modifiers));
   }

   InterfaceModifierStep modifier(Set<C_Modifier> modifiers);

   InterfaceModifierStep abstract_();

   InterfaceModifierStep public_();

   InterfaceModifierStep protected_();

   InterfaceModifierStep private_();

   InterfaceModifierStep sealed();

   InterfaceModifierStep nonSealed();

   InterfaceModifierStep static_();

   InterfaceModifierStep strictfp_();
}

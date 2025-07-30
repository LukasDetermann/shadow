package io.determann.shadow.api.dsl.enum_;

import io.determann.shadow.api.Modifier;

import java.util.Set;

public interface EnumModifierStep
      extends EnumNameStep
{
   EnumModifierStep modifier(String... modifiers);

   default EnumModifierStep modifier(Modifier... modifiers)
   {
      return modifier(Set.of(modifiers));
   }

   EnumModifierStep modifier(Set<Modifier> modifiers);

   EnumModifierStep public_();

   EnumModifierStep protected_();

   EnumModifierStep private_();

   EnumModifierStep static_();

   EnumModifierStep strictfp_();
}

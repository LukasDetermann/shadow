package io.determann.shadow.api.dsl.class_;

import io.determann.shadow.api.Modifier;

import java.util.Set;

public interface ClassModifierStep
      extends ClassNameStep
{
   ClassModifierStep modifier(String... modifiers);

   default ClassModifierStep modifier(Modifier... modifiers)
   {
      return modifier(Set.of(modifiers));
   }

   ClassModifierStep modifier(Set<Modifier> modifiers);

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

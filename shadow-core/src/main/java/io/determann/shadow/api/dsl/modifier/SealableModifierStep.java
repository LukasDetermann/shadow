package io.determann.shadow.api.dsl.modifier;

import io.determann.shadow.api.shadow.modifier.C_Modifier;

public interface SealableModifierStep
      extends ClassModifierStep,
              InterfaceModifierStep
{
   @Override
   SealableModifierStep modifier(String... modifiers);

   @Override
   SealableModifierStep modifier(C_Modifier... modifiers);

   @Override
   SealableModifierStep abstract_();

   @Override
   SealableModifierStep public_();

   @Override
   SealableModifierStep protected_();

   @Override
   SealableModifierStep private_();

   @Override
   SealableModifierStep sealed();

   @Override
   SealableModifierStep nonSealed();

   @Override
   SealableModifierStep static_();

   @Override
   SealableModifierStep strictfp_();
}

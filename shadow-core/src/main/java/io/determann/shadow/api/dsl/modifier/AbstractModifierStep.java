package io.determann.shadow.api.dsl.modifier;

import io.determann.shadow.api.shadow.modifier.C_Modifier;

public interface AbstractModifierStep
      extends SealableModifierStep,
              MethodModifierStep
{

   @Override
   AbstractModifierStep modifier(String... modifiers);

   @Override
   AbstractModifierStep modifier(C_Modifier... modifiers);

   @Override
   AbstractModifierStep abstract_();

   @Override
   AbstractModifierStep public_();

   @Override
   AbstractModifierStep protected_();

   @Override
   AbstractModifierStep private_();

   @Override
   AbstractModifierStep final_();

   @Override
   AbstractModifierStep static_();

   @Override
   AbstractModifierStep strictfp_();
}

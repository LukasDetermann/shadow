package io.determann.shadow.api.dsl.modifier;

import io.determann.shadow.api.shadow.modifier.C_Modifier;

public interface FinalModifierStep
      extends ClassModifierStep,
              FieldModifierStep,
              MethodModifierStep,
              ParameterModifierStep,
              RecordModifierStep
{
   @Override
   FinalModifierStep modifier(String... modifiers);

   @Override
   FinalModifierStep modifier(C_Modifier... modifiers);

   @Override
   FinalModifierStep abstract_();

   @Override
   FinalModifierStep public_();

   @Override
   FinalModifierStep protected_();

   @Override
   FinalModifierStep private_();

   @Override
   FinalModifierStep final_();

   @Override
   FinalModifierStep static_();

   @Override
   FinalModifierStep strictfp_();
}

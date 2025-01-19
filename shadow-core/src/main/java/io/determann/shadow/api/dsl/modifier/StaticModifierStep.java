package io.determann.shadow.api.dsl.modifier;

import io.determann.shadow.api.shadow.modifier.C_Modifier;

public interface StaticModifierStep
      extends StrictfpModifierStep,
              FieldModifierStep
{
   @Override
   StaticModifierStep modifier(String... modifiers);

   @Override
   StaticModifierStep modifier(C_Modifier... modifiers);

   @Override
   StaticModifierStep public_();

   @Override
   StaticModifierStep protected_();

   @Override
   StaticModifierStep private_();

   @Override
   StaticModifierStep static_();

   @Override
   StaticModifierStep final_();
}

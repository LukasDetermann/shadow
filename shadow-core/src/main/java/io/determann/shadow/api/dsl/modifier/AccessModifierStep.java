package io.determann.shadow.api.dsl.modifier;

import io.determann.shadow.api.shadow.modifier.C_Modifier;

public interface AccessModifierStep
      extends StaticModifierStep,
              ConstructorModifierStep
{
   @Override
   AccessModifierStep modifier(String... modifiers);

   @Override
   AccessModifierStep modifier(C_Modifier... modifiers);

   @Override
   AccessModifierStep public_();

   @Override
   AccessModifierStep protected_();

   @Override
   AccessModifierStep private_();
}

package io.determann.shadow.api.dsl.modifier;

import io.determann.shadow.api.shadow.modifier.C_Modifier;

public interface ParameterModifierStep
{
   ParameterModifierStep modifier(String... modifiers);

   ParameterModifierStep modifier(C_Modifier... modifiers);

   ParameterModifierStep final_();
}

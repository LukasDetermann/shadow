package io.determann.shadow.api.dsl;

import io.determann.shadow.api.dsl.modifier.*;
import io.determann.shadow.api.shadow.modifier.C_Modifier;

public interface ModifierStep extends ClassNameStep
{
   ModifierStep modifier(String... modifiers);

   ModifierStep modifier(C_Modifier... modifiers);

   AbstractModifierStep abstract_();

   AccessModifierStep public_();

   AccessModifierStep protected_();

   AccessModifierStep private_();

   DefaultModifierStep default_();

   FinalModifierStep final_();

   NativModifierStep native_();

   SealableModifierStep sealed();

   SealableModifierStep nonSealed();

   StaticModifierStep static_();

   StrictfpModifierStep strictfp_();
}
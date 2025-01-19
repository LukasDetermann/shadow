package io.determann.shadow.api.dsl.modifier;

import io.determann.shadow.api.shadow.modifier.C_Modifier;

public interface StrictfpModifierStep
      extends AnnotationModifierStep,
              ClassModifierStep,
              EnumModifierStep,
              InterfaceModifierStep,
              MethodModifierStep,
              RecordModifierStep
{
   @Override
   StrictfpModifierStep modifier(String... modifiers);

   @Override
   StrictfpModifierStep modifier(C_Modifier... modifiers);

   @Override
   StrictfpModifierStep public_();

   @Override
   StrictfpModifierStep protected_();

   @Override
   StrictfpModifierStep private_();

   @Override
   StrictfpModifierStep static_();

   @Override
   StrictfpModifierStep strictfp_();

   @Override
   StrictfpModifierStep abstract_();

   @Override
   StrictfpModifierStep final_();

   @Override
   StrictfpModifierStep sealed();

   @Override
   StrictfpModifierStep nonSealed();
}

package io.determann.shadow.implementation.support.api.shadow.type;

import io.determann.shadow.api.shadow.type.C_Declared;
import io.determann.shadow.implementation.support.internal.shadow.SupportSupport;

import static io.determann.shadow.api.Operations.MODIFIABLE_GET_MODIFIERS;
import static io.determann.shadow.api.Operations.QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME;

class DeclaredSupport
{
   static <DECLARED extends C_Declared> boolean equals(DECLARED declared, Object other, Class<DECLARED> declaredClass)
   {
      return SupportSupport.equals(declared,
                                   declaredClass,
                                   other,
                                   MODIFIABLE_GET_MODIFIERS,
                                   QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME);
   }

   static int hashCode(C_Declared declared)
   {
      return SupportSupport.hashCode(declared, MODIFIABLE_GET_MODIFIERS, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME);
   }

   static <DECLARED extends C_Declared> String  toString(DECLARED declared, Class<DECLARED> declaredClass)
   {
      return SupportSupport.toString(declared, declaredClass, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME, MODIFIABLE_GET_MODIFIERS);
   }
}

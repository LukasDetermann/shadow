package io.determann.shadow.implementation.support.api.shadow.type;

import io.determann.shadow.api.C;
import io.determann.shadow.implementation.support.internal.shadow.SupportSupport;

import static io.determann.shadow.api.query.Operations.MODIFIABLE_GET_MODIFIERS;
import static io.determann.shadow.api.query.Operations.QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME;

class DeclaredSupport
{
   static <DECLARED extends C.Declared> boolean equals(DECLARED declared, Object other, Class<DECLARED> declaredClass)
   {
      return SupportSupport.equals(declared,
                                   declaredClass,
                                   other,
                                   MODIFIABLE_GET_MODIFIERS,
                                   QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME);
   }

   static int hashCode(C.Declared declared)
   {
      return SupportSupport.hashCode(declared, MODIFIABLE_GET_MODIFIERS, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME);
   }

   static <DECLARED extends C.Declared> String  toString(DECLARED declared, Class<DECLARED> declaredClass)
   {
      return SupportSupport.toString(declared, declaredClass, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME, MODIFIABLE_GET_MODIFIERS);
   }
}

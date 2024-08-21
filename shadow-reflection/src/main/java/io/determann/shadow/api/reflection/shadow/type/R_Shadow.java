package io.determann.shadow.api.reflection.shadow.type;

import io.determann.shadow.api.shadow.C_TypeKind;
import io.determann.shadow.api.shadow.type.C_Shadow;

public interface R_Shadow extends C_Shadow
{
   C_TypeKind getKind();

   default boolean isKind(C_TypeKind typeKind)
   {
      return getKind().equals(typeKind);
   }

   /**
    * type equals from the compiler perspective. for example ? does not equal ? for the compiler
    */
   boolean representsSameType(C_Shadow shadow);
}

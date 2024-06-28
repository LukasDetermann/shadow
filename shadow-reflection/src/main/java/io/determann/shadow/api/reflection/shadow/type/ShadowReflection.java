package io.determann.shadow.api.reflection.shadow.type;

import io.determann.shadow.api.shadow.TypeKind;
import io.determann.shadow.api.shadow.type.Shadow;

public interface ShadowReflection extends Shadow
{
   TypeKind getKind();

   default boolean isKind(TypeKind typeKind)
   {
      return getKind().equals(typeKind);
   }

   /**
    * type equals from the compiler perspective. for example ? does not equal ? for the compiler
    */
   boolean representsSameType(Shadow shadow);
}

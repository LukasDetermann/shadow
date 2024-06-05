package io.determann.shadow.api.reflection.query;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.shadow.Shadow;

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

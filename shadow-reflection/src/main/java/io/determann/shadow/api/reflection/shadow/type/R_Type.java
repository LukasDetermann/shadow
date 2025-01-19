package io.determann.shadow.api.reflection.shadow.type;

import io.determann.shadow.api.reflection.shadow.type.primitive.R_Primitive;
import io.determann.shadow.api.shadow.type.C_Type;

public sealed interface R_Type

      extends C_Type

      permits R_Array,
              R_Declared,
              R_Generic,
              R_Intersection,
              R_Null,
              R_Void,
              R_Wildcard,
              R_Primitive
{
   /**
    * type equals from the compiler perspective. for example ? does not equal ? for the compiler
    */
   boolean representsSameType(C_Type type);
}

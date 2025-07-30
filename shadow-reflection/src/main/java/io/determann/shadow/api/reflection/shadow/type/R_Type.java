package io.determann.shadow.api.reflection.shadow.type;

import io.determann.shadow.api.shadow.type.C_Type;

public sealed interface R_Type
      extends C_Type
      permits R_Null,
              R_VariableType,
              R_Void,
              R_Wildcard
{
   /**
    * type equals from the compiler perspective. for example ? does not equal ? for the compiler
    */
   boolean representsSameType(C_Type type);
}

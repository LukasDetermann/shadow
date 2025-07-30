package io.determann.shadow.api.lang_model.shadow.type;

import io.determann.shadow.api.shadow.type.C_Type;

public sealed interface LM_Type
      extends C_Type
      permits LM_Null,
              LM_VariableType,
              LM_Void,
              LM_Wildcard
{
   /**
    * type equals from the compiler perspective. for example ? does not equal ? for the compiler
    */
   boolean representsSameType(C_Type type);
}

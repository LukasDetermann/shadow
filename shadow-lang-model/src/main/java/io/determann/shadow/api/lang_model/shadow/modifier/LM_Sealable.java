package io.determann.shadow.api.lang_model.shadow.modifier;

import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.modifier.C_Sealable;

public interface LM_Sealable extends LM_Modifiable,
                                     C_Sealable
{
   default boolean isSealed()
   {
      return hasModifier(C_Modifier.SEALED);
   }

   default boolean isNonSealed()
   {
      return hasModifier(C_Modifier.NON_SEALED);
   }
}

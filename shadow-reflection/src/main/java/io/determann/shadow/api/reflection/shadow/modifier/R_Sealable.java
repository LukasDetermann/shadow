package io.determann.shadow.api.reflection.shadow.modifier;

import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.modifier.C_Sealable;

public interface R_Sealable extends R_Modifiable,
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

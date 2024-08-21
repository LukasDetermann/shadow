package io.determann.shadow.api.reflection.shadow.modifier;

import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.modifier.C_StrictfpModifiable;

public interface R_StrictfpModifiable extends R_Modifiable,
                                              C_StrictfpModifiable
{
   default boolean isStrictfp()
   {
      return hasModifier(C_Modifier.STRICTFP);
   }
}

package io.determann.shadow.api.reflection.shadow.modifier;

import io.determann.shadow.api.shadow.modifier.C_FinalModifiable;
import io.determann.shadow.api.shadow.modifier.C_Modifier;

public interface R_FinalModifiable extends R_Modifiable,
                                           C_FinalModifiable
{
   default boolean isFinal()
   {
      return hasModifier(C_Modifier.FINAL);
   }
}

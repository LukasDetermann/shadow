package io.determann.shadow.api.reflection.shadow.modifier;

import io.determann.shadow.api.shadow.modifier.C_DefaultModifiable;
import io.determann.shadow.api.shadow.modifier.C_Modifier;

public interface R_DefaultModifiable extends R_Modifiable,
                                             C_DefaultModifiable
{
   default boolean isDefault()
   {
      return hasModifier(C_Modifier.DEFAULT);
   }
}

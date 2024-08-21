package io.determann.shadow.api.reflection.shadow.modifier;

import io.determann.shadow.api.shadow.modifier.C_AbstractModifiable;
import io.determann.shadow.api.shadow.modifier.C_Modifier;

public interface R_AbstractModifiable extends R_Modifiable,
                                              C_AbstractModifiable
{
   default boolean isAbstract()
   {
      return hasModifier(C_Modifier.ABSTRACT);
   }
}

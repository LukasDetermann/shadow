package io.determann.shadow.api.reflection.shadow.modifier;

import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.modifier.C_StaticModifiable;

public interface R_StaticModifiable extends R_Modifiable,
                                            C_StaticModifiable
{
   default boolean isStatic()
   {
      return hasModifier(C_Modifier.STATIC);
   }
}

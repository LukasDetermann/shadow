package io.determann.shadow.api.lang_model.shadow.modifier;

import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.modifier.C_StaticModifiable;

public interface LM_StaticModifiable extends LM_Modifiable,
                                             C_StaticModifiable
{
   default boolean isStatic()
   {
      return hasModifier(C_Modifier.STATIC);
   }
}

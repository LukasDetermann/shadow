package io.determann.shadow.api.lang_model.shadow.modifier;

import io.determann.shadow.api.shadow.modifier.C_AbstractModifiable;
import io.determann.shadow.api.shadow.modifier.C_Modifier;

public interface LM_AbstractModifiable extends LM_Modifiable,
                                               C_AbstractModifiable
{
   default boolean isAbstract()
   {
      return hasModifier(C_Modifier.ABSTRACT);
   }
}

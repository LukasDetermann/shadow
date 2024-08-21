package io.determann.shadow.api.lang_model.shadow.modifier;

import io.determann.shadow.api.shadow.modifier.C_DefaultModifiable;
import io.determann.shadow.api.shadow.modifier.C_Modifier;

public interface LM_DefaultModifiable extends LM_Modifiable,
                                              C_DefaultModifiable
{
   default boolean isDefault()
   {
      return hasModifier(C_Modifier.DEFAULT);
   }
}

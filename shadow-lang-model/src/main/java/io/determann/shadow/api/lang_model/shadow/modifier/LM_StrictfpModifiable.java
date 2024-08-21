package io.determann.shadow.api.lang_model.shadow.modifier;

import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.modifier.C_StrictfpModifiable;

public interface LM_StrictfpModifiable extends LM_Modifiable,
                                               C_StrictfpModifiable
{
   default boolean isStrictfp()
   {
      return hasModifier(C_Modifier.STRICTFP);
   }
}

package io.determann.shadow.api.lang_model.shadow.modifier;

import io.determann.shadow.api.shadow.modifier.C_FinalModifiable;
import io.determann.shadow.api.shadow.modifier.C_Modifier;

public interface LM_FinalModifiable extends LM_Modifiable,
                                            C_FinalModifiable
{
   default boolean isFinal()
   {
      return hasModifier(C_Modifier.FINAL);
   }
}

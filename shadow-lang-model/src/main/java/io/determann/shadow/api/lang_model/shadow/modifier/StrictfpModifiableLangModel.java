package io.determann.shadow.api.lang_model.shadow.modifier;

import io.determann.shadow.api.shadow.modifier.Modifier;
import io.determann.shadow.api.shadow.modifier.StrictfpModifiable;

public interface StrictfpModifiableLangModel extends ModifiableLangModel,
                                                     StrictfpModifiable
{
   default boolean isStrictfp()
   {
      return hasModifier(Modifier.STRICTFP);
   }
}

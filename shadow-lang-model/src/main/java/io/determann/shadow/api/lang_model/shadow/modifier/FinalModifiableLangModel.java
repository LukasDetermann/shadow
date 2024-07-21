package io.determann.shadow.api.lang_model.shadow.modifier;

import io.determann.shadow.api.shadow.modifier.FinalModifiable;
import io.determann.shadow.api.shadow.modifier.Modifier;

public interface FinalModifiableLangModel extends ModifiableLangModel,
                                                  FinalModifiable
{
   default boolean isFinal()
   {
      return hasModifier(Modifier.FINAL);
   }
}

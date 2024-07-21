package io.determann.shadow.api.lang_model.shadow.modifier;

import io.determann.shadow.api.shadow.modifier.AbstractModifiable;
import io.determann.shadow.api.shadow.modifier.Modifier;

public interface AbstractModifiableLangModel extends ModifiableLangModel,
                                                     AbstractModifiable
{
   default boolean isAbstract()
   {
      return hasModifier(Modifier.ABSTRACT);
   }
}

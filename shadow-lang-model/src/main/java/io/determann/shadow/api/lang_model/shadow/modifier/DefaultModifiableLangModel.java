package io.determann.shadow.api.lang_model.shadow.modifier;

import io.determann.shadow.api.shadow.modifier.DefaultModifiable;
import io.determann.shadow.api.shadow.modifier.Modifier;

public interface DefaultModifiableLangModel extends ModifiableLangModel,
                                                    DefaultModifiable
{
   default boolean isDefault()
   {
      return hasModifier(Modifier.DEFAULT);
   }
}

package io.determann.shadow.api.lang_model.shadow.modifier;

import io.determann.shadow.api.shadow.modifier.Modifier;
import io.determann.shadow.api.shadow.modifier.StaticModifiable;

public interface StaticModifiableLangModel extends ModifiableLangModel,
                                                   StaticModifiable
{
   default boolean isStatic()
   {
      return hasModifier(Modifier.STATIC);
   }
}

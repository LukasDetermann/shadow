package io.determann.shadow.api.lang_model.shadow.modifier;

import io.determann.shadow.api.shadow.modifier.Modifier;
import io.determann.shadow.api.shadow.modifier.Sealable;

public interface SealableLangModel extends ModifiableLangModel,
                                           Sealable
{
   default boolean isSealed()
   {
      return hasModifier(Modifier.SEALED);
   }

   default boolean isNonSealed()
   {
      return hasModifier(Modifier.NON_SEALED);
   }
}

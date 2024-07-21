package io.determann.shadow.api.reflection.shadow.modifier;

import io.determann.shadow.api.shadow.modifier.Modifier;
import io.determann.shadow.api.shadow.modifier.Sealable;

public interface SealableReflection extends ModifiableReflection,
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

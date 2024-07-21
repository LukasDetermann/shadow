package io.determann.shadow.api.reflection.shadow.modifier;

import io.determann.shadow.api.shadow.modifier.Modifier;
import io.determann.shadow.api.shadow.modifier.StrictfpModifiable;

public interface StrictfpModifiableReflection extends ModifiableReflection,
                                                      StrictfpModifiable
{
   default boolean isStrictfp()
   {
      return hasModifier(Modifier.STRICTFP);
   }
}

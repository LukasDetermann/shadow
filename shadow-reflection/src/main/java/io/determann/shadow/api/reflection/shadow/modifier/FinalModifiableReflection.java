package io.determann.shadow.api.reflection.shadow.modifier;

import io.determann.shadow.api.shadow.modifier.FinalModifiable;
import io.determann.shadow.api.shadow.modifier.Modifier;

public interface FinalModifiableReflection extends ModifiableReflection,
                                                   FinalModifiable
{
   default boolean isFinal()
   {
      return hasModifier(Modifier.FINAL);
   }
}

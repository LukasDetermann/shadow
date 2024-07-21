package io.determann.shadow.api.reflection.shadow.modifier;

import io.determann.shadow.api.shadow.modifier.AbstractModifiable;
import io.determann.shadow.api.shadow.modifier.Modifier;

public interface AbstractModifiableReflection extends ModifiableReflection,
                                                      AbstractModifiable
{
   default boolean isAbstract()
   {
      return hasModifier(Modifier.ABSTRACT);
   }
}

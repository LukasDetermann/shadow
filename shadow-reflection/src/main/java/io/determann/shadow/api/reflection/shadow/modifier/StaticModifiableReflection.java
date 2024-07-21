package io.determann.shadow.api.reflection.shadow.modifier;

import io.determann.shadow.api.shadow.modifier.Modifier;
import io.determann.shadow.api.shadow.modifier.StaticModifiable;

public interface StaticModifiableReflection extends ModifiableReflection,
                                                    StaticModifiable
{
   default boolean isStatic()
   {
      return hasModifier(Modifier.STATIC);
   }
}

package io.determann.shadow.api.reflection.shadow.modifier;

import io.determann.shadow.api.shadow.modifier.DefaultModifiable;
import io.determann.shadow.api.shadow.modifier.Modifier;

public interface DefaultModifiableReflection extends ModifiableReflection,
                                                     DefaultModifiable
{
   default boolean isDefault()
   {
      return hasModifier(Modifier.DEFAULT);
   }
}

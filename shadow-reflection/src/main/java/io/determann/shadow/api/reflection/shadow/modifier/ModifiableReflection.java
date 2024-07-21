package io.determann.shadow.api.reflection.shadow.modifier;

import io.determann.shadow.api.shadow.modifier.Modifiable;
import io.determann.shadow.api.shadow.modifier.Modifier;

import java.util.Set;

public interface ModifiableReflection extends Modifiable
{
   Set<Modifier> getModifiers();

   default boolean hasModifier(Modifier modifier)
   {
      return getModifiers().contains(modifier);
   }
}

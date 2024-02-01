package io.determann.shadow.api.modifier;

import java.util.Set;

public interface Modifiable
{
   Set<Modifier> getModifiers();

   default boolean hasModifier(Modifier modifier)
   {
      return getModifiers().contains(modifier);
   }
}

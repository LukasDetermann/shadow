package org.determann.shadow.api.modifier;

import org.jetbrains.annotations.UnmodifiableView;

import java.util.Set;

public interface Modifiable
{
   @UnmodifiableView Set<Modifier> getModifiers();

   default boolean hasModifier(Modifier modifier)
   {
      return getModifiers().contains(modifier);
   }
}

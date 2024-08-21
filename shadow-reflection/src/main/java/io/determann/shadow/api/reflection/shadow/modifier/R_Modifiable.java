package io.determann.shadow.api.reflection.shadow.modifier;

import io.determann.shadow.api.shadow.modifier.C_Modifiable;
import io.determann.shadow.api.shadow.modifier.C_Modifier;

import java.util.Set;

public interface R_Modifiable extends C_Modifiable
{
   Set<C_Modifier> getModifiers();

   default boolean hasModifier(C_Modifier modifier)
   {
      return getModifiers().contains(modifier);
   }
}

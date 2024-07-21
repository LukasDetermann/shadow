package io.determann.shadow.api.lang_model.shadow.modifier;

import io.determann.shadow.api.shadow.modifier.Modifiable;
import io.determann.shadow.api.shadow.modifier.Modifier;

import java.util.Set;

public interface ModifiableLangModel extends Modifiable
{
   Set<Modifier> getModifiers();

   default boolean hasModifier(Modifier modifier)
   {
      return getModifiers().contains(modifier);
   }
}

package org.determann.shadow.api.modifier;

public interface FinalModifiable extends Modifiable
{
   default boolean isFinal()
   {
      return hasModifier(Modifier.FINAL);
   }
}

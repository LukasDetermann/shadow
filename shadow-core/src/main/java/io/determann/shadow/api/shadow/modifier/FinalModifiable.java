package io.determann.shadow.api.shadow.modifier;

public interface FinalModifiable extends Modifiable
{
   default boolean isFinal()
   {
      return hasModifier(Modifier.FINAL);
   }
}

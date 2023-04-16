package io.determann.shadow.api.modifier;

public interface StrictfpModifiable extends Modifiable
{
   default boolean isStrictfp()
   {
      return hasModifier(Modifier.STRICTFP);
   }
}

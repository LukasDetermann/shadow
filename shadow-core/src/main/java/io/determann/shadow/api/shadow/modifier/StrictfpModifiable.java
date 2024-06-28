package io.determann.shadow.api.shadow.modifier;

public interface StrictfpModifiable extends Modifiable
{
   default boolean isStrictfp()
   {
      return hasModifier(Modifier.STRICTFP);
   }
}

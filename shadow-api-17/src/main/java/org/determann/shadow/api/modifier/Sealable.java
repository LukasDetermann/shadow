package org.determann.shadow.api.modifier;

public interface Sealable extends Modifiable
{
   default boolean isSealed()
   {
      return hasModifier(Modifier.SEALED);
   }

   default boolean isNonSealed()
   {
      return hasModifier(Modifier.NON_SEALED);
   }
}

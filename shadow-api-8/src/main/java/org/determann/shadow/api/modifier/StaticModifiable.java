package org.determann.shadow.api.modifier;

public interface StaticModifiable extends Modifiable
{
   default boolean isStatic()
   {
      return hasModifier(Modifier.STATIC);
   }
}

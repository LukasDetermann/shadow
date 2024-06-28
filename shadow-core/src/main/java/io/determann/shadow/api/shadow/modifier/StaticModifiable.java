package io.determann.shadow.api.shadow.modifier;

public interface StaticModifiable extends Modifiable
{
   default boolean isStatic()
   {
      return hasModifier(Modifier.STATIC);
   }
}

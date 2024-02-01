package io.determann.shadow.api.modifier;

public interface AbstractModifiable extends Modifiable
{
   default boolean isAbstract()
   {
      return hasModifier(Modifier.ABSTRACT);
   }
}

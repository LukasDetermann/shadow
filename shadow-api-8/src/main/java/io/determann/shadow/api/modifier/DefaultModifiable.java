package io.determann.shadow.api.modifier;

public interface DefaultModifiable extends Modifiable
{
   default boolean isDefault()
   {
      return hasModifier(Modifier.DEFAULT);
   }
}

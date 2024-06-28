package io.determann.shadow.api.shadow.modifier;

public interface DefaultModifiable extends Modifiable
{
   default boolean isDefault()
   {
      return hasModifier(Modifier.DEFAULT);
   }
}

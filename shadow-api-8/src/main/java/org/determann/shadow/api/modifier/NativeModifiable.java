package org.determann.shadow.api.modifier;

public interface NativeModifiable extends Modifiable
{
   default boolean isNative()
   {
      return hasModifier(Modifier.NATIVE);
   }
}

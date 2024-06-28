package io.determann.shadow.api.shadow.modifier;

public interface NativeModifiable extends Modifiable
{
   default boolean isNative()
   {
      return hasModifier(Modifier.NATIVE);
   }
}

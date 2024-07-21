package io.determann.shadow.api.reflection.shadow.modifier;

import io.determann.shadow.api.shadow.modifier.Modifier;
import io.determann.shadow.api.shadow.modifier.NativeModifiable;

public interface NativeModifiableReflection extends ModifiableReflection,
                                                    NativeModifiable
{
   default boolean isNative()
   {
      return hasModifier(Modifier.NATIVE);
   }
}

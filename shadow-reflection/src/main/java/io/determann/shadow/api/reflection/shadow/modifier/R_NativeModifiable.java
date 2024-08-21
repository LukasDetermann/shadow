package io.determann.shadow.api.reflection.shadow.modifier;

import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.modifier.C_NativeModifiable;

public interface R_NativeModifiable extends R_Modifiable,
                                            C_NativeModifiable
{
   default boolean isNative()
   {
      return hasModifier(C_Modifier.NATIVE);
   }
}

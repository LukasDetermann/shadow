package io.determann.shadow.api.lang_model.shadow.modifier;

import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.modifier.C_NativeModifiable;

public interface LM_NativeModifiable extends LM_Modifiable,
                                             C_NativeModifiable
{
   default boolean isNative()
   {
      return  hasModifier(C_Modifier.NATIVE);
   }
}

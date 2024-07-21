package io.determann.shadow.api.lang_model.shadow.modifier;

import io.determann.shadow.api.shadow.modifier.Modifier;
import io.determann.shadow.api.shadow.modifier.NativeModifiable;

public interface NativeModifiableLangModel extends ModifiableLangModel,
                                                   NativeModifiable
{
   default boolean isNative()
   {
      return  hasModifier(Modifier.NATIVE);
   }
}

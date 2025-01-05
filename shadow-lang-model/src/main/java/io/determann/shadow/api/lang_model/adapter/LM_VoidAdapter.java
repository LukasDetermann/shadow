package io.determann.shadow.api.lang_model.adapter;

import io.determann.shadow.api.lang_model.shadow.type.LM_Void;
import io.determann.shadow.internal.lang_model.shadow.type.VoidImpl;

import javax.lang.model.type.NoType;

public class LM_VoidAdapter
{
   private final LM_Void aVoid;

   LM_VoidAdapter(LM_Void aVoid)
   {
      this.aVoid = aVoid;
   }

   public NoType toNoType()
   {
      //noinspection OverlyStrongTypeCast
      return ((VoidImpl) aVoid).getMirror();
   }
}

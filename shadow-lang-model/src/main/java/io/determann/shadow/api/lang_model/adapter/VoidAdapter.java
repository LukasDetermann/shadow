package io.determann.shadow.api.lang_model.adapter;

import io.determann.shadow.api.lang_model.LM;
import io.determann.shadow.internal.lang_model.shadow.type.VoidImpl;

import javax.lang.model.type.NoType;

public class VoidAdapter
{
   private final LM.Void aVoid;

   VoidAdapter(LM.Void aVoid)
   {
      this.aVoid = aVoid;
   }

   public NoType toNoType()
   {
      //noinspection OverlyStrongTypeCast
      return ((VoidImpl) aVoid).getMirror();
   }
}

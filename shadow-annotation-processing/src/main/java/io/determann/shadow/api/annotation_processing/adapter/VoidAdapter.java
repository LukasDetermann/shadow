package io.determann.shadow.api.annotation_processing.adapter;

import io.determann.shadow.api.annotation_processing.AP;
import io.determann.shadow.internal.annotation_processing.shadow.type.VoidImpl;

import javax.lang.model.type.NoType;

public class VoidAdapter
{
   private final AP.Void aVoid;

   VoidAdapter(AP.Void aVoid)
   {
      this.aVoid = aVoid;
   }

   public NoType toNoType()
   {
      //noinspection OverlyStrongTypeCast
      return ((VoidImpl) aVoid).getMirror();
   }
}

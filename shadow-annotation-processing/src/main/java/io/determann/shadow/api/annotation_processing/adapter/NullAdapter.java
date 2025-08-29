package io.determann.shadow.api.annotation_processing.adapter;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.internal.annotation_processing.shadow.type.NullImpl;

import javax.lang.model.type.NullType;

public class NullAdapter
{
   private final Ap.Null aNull;

   NullAdapter(Ap.Null aNull)
   {
      this.aNull = aNull;
   }

   public NullType toNullType()
   {
      //noinspection OverlyStrongTypeCast
      return ((NullImpl) aNull).getMirror();
   }
}

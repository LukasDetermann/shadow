package io.determann.shadow.api.annotation_processing.adapter;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.internal.annotation_processing.shadow.type.PrimitiveImpl;

import javax.lang.model.type.PrimitiveType;

public class PrimitiveAdapter
{
   private final Ap.Primitive primitive;

   PrimitiveAdapter(Ap.Primitive primitive)
   {
      this.primitive = primitive;
   }

   public PrimitiveType toPrimitiveType()
   {
      //noinspection OverlyStrongTypeCast
      return ((PrimitiveImpl) primitive).getMirror();
   }
}

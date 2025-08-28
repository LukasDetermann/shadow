package io.determann.shadow.api.annotation_processing.adapter;

import io.determann.shadow.api.annotation_processing.AP;
import io.determann.shadow.internal.annotation_processing.shadow.type.primitive.PrimitiveImpl;

import javax.lang.model.type.PrimitiveType;

public class PrimitiveAdapter
{
   private final AP.Primitive primitive;

   PrimitiveAdapter(AP.Primitive primitive)
   {
      this.primitive = primitive;
   }

   public PrimitiveType toPrimitiveType()
   {
      //noinspection OverlyStrongTypeCast
      return ((PrimitiveImpl) primitive).getMirror();
   }
}

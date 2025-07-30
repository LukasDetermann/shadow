package io.determann.shadow.api.lang_model.adapter;

import io.determann.shadow.api.lang_model.LM;
import io.determann.shadow.internal.lang_model.shadow.type.primitive.PrimitiveImpl;

import javax.lang.model.type.PrimitiveType;

public class PrimitiveAdapter
{
   private final LM.Primitive primitive;

   PrimitiveAdapter(LM.Primitive primitive)
   {
      this.primitive = primitive;
   }

   public PrimitiveType toPrimitiveType()
   {
      //noinspection OverlyStrongTypeCast
      return ((PrimitiveImpl) primitive).getMirror();
   }
}

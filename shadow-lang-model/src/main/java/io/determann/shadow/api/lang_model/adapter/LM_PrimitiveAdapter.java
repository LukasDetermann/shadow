package io.determann.shadow.api.lang_model.adapter;

import io.determann.shadow.api.lang_model.shadow.type.primitive.LM_Primitive;
import io.determann.shadow.internal.lang_model.shadow.type.primitive.PrimitiveImpl;

import javax.lang.model.type.PrimitiveType;

public class LM_PrimitiveAdapter
{
   private final LM_Primitive primitive;

   LM_PrimitiveAdapter(LM_Primitive primitive)
   {
      this.primitive = primitive;
   }

   public PrimitiveType toPrimitiveType()
   {
      //noinspection OverlyStrongTypeCast
      return ((PrimitiveImpl) primitive).getMirror();
   }
}

package com.derivandi.api.adapter;

import com.derivandi.api.Ap;
import com.derivandi.internal.shadow.type.PrimitiveImpl;

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

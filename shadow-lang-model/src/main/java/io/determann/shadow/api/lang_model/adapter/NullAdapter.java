package io.determann.shadow.api.lang_model.adapter;

import io.determann.shadow.api.lang_model.LM;
import io.determann.shadow.internal.lang_model.shadow.type.NullImpl;

import javax.lang.model.type.NullType;

public class NullAdapter
{
   private final LM.Null aNull;

   NullAdapter(LM.Null aNull)
   {
      this.aNull = aNull;
   }

   public NullType toNullType()
   {
      //noinspection OverlyStrongTypeCast
      return ((NullImpl) aNull).getMirror();
   }
}

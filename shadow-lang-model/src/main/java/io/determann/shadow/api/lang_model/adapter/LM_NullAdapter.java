package io.determann.shadow.api.lang_model.adapter;

import io.determann.shadow.api.lang_model.shadow.type.LM_Null;
import io.determann.shadow.internal.lang_model.shadow.type.NullImpl;

import javax.lang.model.type.NullType;

public class LM_NullAdapter
{
   private final LM_Null aNull;

   LM_NullAdapter(LM_Null aNull)
   {
      this.aNull = aNull;
   }

   public NullType toNullType()
   {
      //noinspection OverlyStrongTypeCast
      return ((NullImpl) aNull).getMirror();
   }
}

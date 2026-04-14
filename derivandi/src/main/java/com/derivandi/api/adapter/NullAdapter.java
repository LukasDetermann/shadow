package com.derivandi.api.adapter;

import com.derivandi.api.Ap;
import com.derivandi.internal.shadow.type.NullImpl;

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

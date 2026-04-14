package com.derivandi.api.adapter;

import com.derivandi.api.Ap;
import com.derivandi.internal.shadow.type.VoidImpl;

import javax.lang.model.type.NoType;

public class VoidAdapter
{
   private final Ap.Void aVoid;

   VoidAdapter(Ap.Void aVoid)
   {
      this.aVoid = aVoid;
   }

   public NoType toNoType()
   {
      //noinspection OverlyStrongTypeCast
      return ((VoidImpl) aVoid).getMirror();
   }
}

package com.derivandi.api.adapter;

import com.derivandi.api.Ap;
import com.derivandi.internal.shadow.type.WildcardImpl;

import javax.lang.model.type.WildcardType;

public class WildcardAdapter
{
   private final Ap.Wildcard wildcard;

   WildcardAdapter(Ap.Wildcard wildcard)
   {
      this.wildcard = wildcard;
   }

   public WildcardType toWildcardType()
   {
      //noinspection OverlyStrongTypeCast
      return ((WildcardImpl) wildcard).getMirror();
   }
}

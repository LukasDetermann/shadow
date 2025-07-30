package io.determann.shadow.api.lang_model.adapter;

import io.determann.shadow.api.lang_model.LM;
import io.determann.shadow.internal.lang_model.shadow.type.WildcardImpl;

import javax.lang.model.type.WildcardType;

public class WildcardAdapter
{
   private final LM.Wildcard wildcard;

   WildcardAdapter(LM.Wildcard wildcard)
   {
      this.wildcard = wildcard;
   }

   public WildcardType toWildcardType()
   {
      //noinspection OverlyStrongTypeCast
      return ((WildcardImpl) wildcard).getMirror();
   }
}

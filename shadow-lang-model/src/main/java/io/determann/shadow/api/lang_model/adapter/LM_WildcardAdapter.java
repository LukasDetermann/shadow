package io.determann.shadow.api.lang_model.adapter;

import io.determann.shadow.api.lang_model.shadow.type.LM_Wildcard;
import io.determann.shadow.internal.lang_model.shadow.type.WildcardImpl;

import javax.lang.model.type.WildcardType;

public class LM_WildcardAdapter
{
   private final LM_Wildcard wildcard;

   LM_WildcardAdapter(LM_Wildcard wildcard)
   {
      this.wildcard = wildcard;
   }

   public WildcardType toWildcardType()
   {
      //noinspection OverlyStrongTypeCast
      return ((WildcardImpl) wildcard).getMirror();
   }
}

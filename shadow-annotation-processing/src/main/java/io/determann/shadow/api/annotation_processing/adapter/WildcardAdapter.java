package io.determann.shadow.api.annotation_processing.adapter;

import io.determann.shadow.api.annotation_processing.AP;
import io.determann.shadow.internal.annotation_processing.shadow.type.WildcardImpl;

import javax.lang.model.type.WildcardType;

public class WildcardAdapter
{
   private final AP.Wildcard wildcard;

   WildcardAdapter(AP.Wildcard wildcard)
   {
      this.wildcard = wildcard;
   }

   public WildcardType toWildcardType()
   {
      //noinspection OverlyStrongTypeCast
      return ((WildcardImpl) wildcard).getMirror();
   }
}

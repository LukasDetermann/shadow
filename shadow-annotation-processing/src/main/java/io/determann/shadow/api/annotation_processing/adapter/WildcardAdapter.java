package io.determann.shadow.api.annotation_processing.adapter;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.internal.annotation_processing.shadow.type.WildcardImpl;

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

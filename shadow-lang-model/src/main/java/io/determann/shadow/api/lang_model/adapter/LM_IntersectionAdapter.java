package io.determann.shadow.api.lang_model.adapter;

import io.determann.shadow.api.lang_model.shadow.type.LM_Intersection;
import io.determann.shadow.internal.lang_model.shadow.type.IntersectionImpl;

import javax.lang.model.type.IntersectionType;

public class LM_IntersectionAdapter
{
   private final LM_Intersection intersection;

   LM_IntersectionAdapter(LM_Intersection intersection)
   {
      this.intersection = intersection;
   }

   public IntersectionType toIntersectionType()
   {
      //noinspection OverlyStrongTypeCast
      return ((IntersectionImpl) intersection).getMirror();
   }
}

package io.determann.shadow.internal.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.LM_Adapter;
import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.shadow.type.LM_Intersection;
import io.determann.shadow.api.lang_model.shadow.type.LM_Type;
import io.determann.shadow.implementation.support.api.shadow.type.IntersectionSupport;

import javax.lang.model.type.IntersectionType;
import java.util.List;

public class IntersectionImpl extends TypeImpl<IntersectionType> implements LM_Intersection
{

   public IntersectionImpl(LM_Context context, IntersectionType intersectionType)
   {
      super(context, intersectionType);
   }

   @Override
   public List<LM_Type> getBounds()
   {
      return getMirror().getBounds().stream()
                        .map(typeMirror -> LM_Adapter.<LM_Type>generalize(getApi(), typeMirror))
                        .toList();
   }

   @Override
   public int hashCode()
   {
      return IntersectionSupport.hashCode(this);
   }

   @Override
   public boolean equals(Object other)
   {
      return IntersectionSupport.equals(this, other);
   }

   @Override
   public String toString()
   {
      return IntersectionSupport.toString(this);
   }
}

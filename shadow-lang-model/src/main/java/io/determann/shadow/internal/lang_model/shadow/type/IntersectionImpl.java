package io.determann.shadow.internal.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.LM_Adapter;
import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.shadow.type.LM_Array;
import io.determann.shadow.api.lang_model.shadow.type.LM_Intersection;
import io.determann.shadow.api.lang_model.shadow.type.LM_Type;
import io.determann.shadow.implementation.support.api.shadow.type.IntersectionSupport;

import javax.lang.model.type.IntersectionType;
import java.util.List;

import static io.determann.shadow.api.lang_model.LM_Adapter.generalize;
import static io.determann.shadow.api.lang_model.LM_Adapter.getTypes;

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
   public LM_Intersection erasure()
   {
      return generalize(getApi(), getTypes(getApi()).erasure(getMirror()));
   }

   @Override
   public LM_Array asArray()
   {
      return LM_Adapter.generalize(getApi(), LM_Adapter.getTypes(getApi()).getArrayType(getMirror()));
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

package io.determann.shadow.internal.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.LM_Adapter;
import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.shadow.type.LM_Array;
import io.determann.shadow.api.lang_model.shadow.type.LM_Shadow;
import io.determann.shadow.api.shadow.C_TypeKind;
import io.determann.shadow.api.shadow.type.C_Shadow;
import io.determann.shadow.implementation.support.api.shadow.type.ArraySupport;

import javax.lang.model.type.ArrayType;
import java.util.List;

public final class ArrayImpl extends ShadowImpl<ArrayType> implements LM_Array
{

   public ArrayImpl(LM_Context context, ArrayType arrayType)
   {
      super(context, arrayType);
   }

   @Override
   public boolean isSubtypeOf(C_Shadow shadow)
   {
      return LM_Adapter.getTypes(getApi()).isSubtype(LM_Adapter.particularType((LM_Shadow) shadow), getMirror());
   }

   @Override
   public LM_Shadow getComponentType()
   {
      return LM_Adapter.generalize(getApi(), getMirror().getComponentType());
   }

   @Override
   public List<LM_Shadow> getDirectSuperTypes()
   {
      return LM_Adapter.getTypes(getApi())
                       .directSupertypes(getMirror())
                       .stream()
                       .map(typeMirror1 -> LM_Adapter.<LM_Shadow>generalize(getApi(), typeMirror1))
                       .toList();
   }

   @Override
   public C_TypeKind getKind()
   {
      return C_TypeKind.ARRAY;
   }

   @Override
   public boolean representsSameType(C_Shadow shadow)
   {
      return ArraySupport.representsSameType(this, shadow);
   }

   @Override
   public int hashCode()
   {
      return ArraySupport.hashCode(this);
   }

   @Override
   public boolean equals(Object other)
   {
      return ArraySupport.equals(this, other);
   }

   @Override
   public String toString()
   {
      return ArraySupport.toString(this);
   }
}

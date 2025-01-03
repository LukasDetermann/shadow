package io.determann.shadow.internal.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.LM_Adapter;
import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.shadow.type.LM_Array;
import io.determann.shadow.api.lang_model.shadow.type.LM_Type;
import io.determann.shadow.api.lang_model.shadow.type.LM_Wildcard;
import io.determann.shadow.api.shadow.type.C_Type;
import io.determann.shadow.implementation.support.api.shadow.type.ArraySupport;

import javax.lang.model.type.ArrayType;
import java.util.List;

import static io.determann.shadow.api.lang_model.LM_Adapter.*;

public final class ArrayImpl extends TypeImpl<ArrayType> implements LM_Array
{

   public ArrayImpl(LM_Context context, ArrayType arrayType)
   {
      super(context, arrayType);
   }

   @Override
   public boolean isSubtypeOf(C_Type type)
   {
      return getTypes(getApi()).isSubtype(particularType((LM_Type) type), getMirror());
   }

   @Override
   public LM_Type getComponentType()
   {
      return generalize(getApi(), getMirror().getComponentType());
   }

   @Override
   public List<LM_Type> getDirectSuperTypes()
   {
      return getTypes(getApi())
                       .directSupertypes(getMirror())
                       .stream()
                       .map(typeMirror1 -> LM_Adapter.<LM_Type>generalize(getApi(), typeMirror1))
                       .toList();
   }

   @Override
   public LM_Array asArray()
   {
      return LM_Adapter.generalize(getApi(), LM_Adapter.getTypes(getApi()).getArrayType(getMirror()));
   }

   @Override
   public LM_Wildcard asExtendsWildcard()
   {
      return LM_Adapter.generalize(getApi(), LM_Adapter.getTypes(getApi()).getWildcardType(getMirror(), null));
   }

   @Override
   public LM_Wildcard asSuperWildcard()
   {
      return LM_Adapter.generalize(getApi(), LM_Adapter.getTypes(getApi()).getWildcardType(null, getMirror()));
   }

   @Override
   public boolean representsSameType(C_Type type)
   {
      return ArraySupport.representsSameType(this, type);
   }

   @Override
   public LM_Array erasure()
   {
      return generalize(getApi(), getTypes(getApi()).erasure(getMirror()));
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

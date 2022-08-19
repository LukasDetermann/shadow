package org.determann.shadow.impl.shadow;

import org.determann.shadow.api.ShadowApi;
import org.determann.shadow.api.TypeKind;
import org.determann.shadow.api.shadow.Array;
import org.determann.shadow.api.shadow.Shadow;

import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Objects;

public final class ArrayImpl extends ShadowImpl<ArrayType> implements Array
{

   public ArrayImpl(ShadowApi shadowApi, ArrayType arrayType)
   {
      super(shadowApi, arrayType);
   }

   @Override
   public boolean isSubtypeOf(Shadow<? extends TypeMirror> shadow)
   {
      return getApi().getJdkApiContext().types().isSubtype(shadow.getMirror(), getMirror());
   }

   @Override
   public Shadow<TypeMirror> getComponentType()
   {
      return getApi().getShadowFactory().shadowFromType(getMirror().getComponentType());
   }

   @Override
   public List<Shadow<TypeMirror>> getDirectSuperTypes()
   {
      return getApi().getJdkApiContext().types()
                     .directSupertypes(getMirror())
                     .stream()
                     .map(typeMirror1 -> getApi().getShadowFactory().<Shadow<TypeMirror>>shadowFromType(typeMirror1))
                     .toList();
   }

   @Override
   public TypeKind getTypeKind()
   {
      return TypeKind.ARRAY;
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getComponentType());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (other == null || !getClass().equals(other.getClass()))
      {
         return false;
      }
      Array otherArray = (Array) other;
      return Objects.equals(getComponentType(), otherArray.getComponentType());
   }
}

package org.determann.shadow.impl.shadow;

import org.determann.shadow.api.ShadowApi;
import org.determann.shadow.api.TypeKind;
import org.determann.shadow.api.shadow.Array;
import org.determann.shadow.api.shadow.Shadow;
import org.determann.shadow.api.shadow.Wildcard;

import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeMirror;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

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
                     .collect(collectingAndThen(toList(), Collections::unmodifiableList));
   }

   @Override
   public Array erasure()
   {
      return getApi().getShadowFactory().shadowFromType(getApi().getJdkApiContext().types().erasure(getMirror()));
   }

   @Override
   public Wildcard asExtendsWildcard()
   {
      return getApi().getShadowFactory().shadowFromType(getApi().getJdkApiContext().types().getWildcardType(getMirror(), null));
   }

   @Override
   public Wildcard asSuperWildcard()
   {
      return getApi().getShadowFactory().shadowFromType(getApi().getJdkApiContext().types().getWildcardType(null, getMirror()));
   }

   @Override
   public Array asArray()
   {
      return getApi().getShadowFactory().shadowFromType(getApi().getJdkApiContext().types().getArrayType(getMirror()));
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

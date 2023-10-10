package io.determann.shadow.impl.shadow;

import io.determann.shadow.api.MirrorAdapter;
import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.shadow.Array;
import io.determann.shadow.api.shadow.Shadow;
import io.determann.shadow.api.shadow.Wildcard;

import javax.lang.model.type.ArrayType;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toUnmodifiableList;

public final class ArrayImpl extends ShadowImpl<ArrayType> implements Array
{

   public ArrayImpl(ShadowApi shadowApi, ArrayType arrayType)
   {
      super(shadowApi, arrayType);
   }

   @Override
   public boolean isSubtypeOf(Shadow shadow)
   {
      return getApi().getJdkApiContext().getProcessingEnv().getTypeUtils().isSubtype(MirrorAdapter.getType(shadow), getMirror());
   }

   @Override
   public Shadow getComponentType()
   {
      return MirrorAdapter.getShadow(getApi(), getMirror().getComponentType());
   }

   @Override
   public List<Shadow> getDirectSuperTypes()
   {
      return getApi().getJdkApiContext().getProcessingEnv().getTypeUtils()
                     .directSupertypes(getMirror())
                     .stream()
                     .map(typeMirror1 -> MirrorAdapter.<Shadow>getShadow(getApi(), typeMirror1))
                     .collect(toUnmodifiableList());
   }

   @Override
   public Array erasure()
   {
      return MirrorAdapter.getShadow(getApi(), getApi().getJdkApiContext().getProcessingEnv().getTypeUtils().erasure(getMirror()));
   }

   @Override
   public Wildcard asExtendsWildcard()
   {
      return MirrorAdapter
                     .getShadow(getApi(), getApi().getJdkApiContext().getProcessingEnv().getTypeUtils().getWildcardType(getMirror(), null));
   }

   @Override
   public Wildcard asSuperWildcard()
   {
      return MirrorAdapter
                     .getShadow(getApi(), getApi().getJdkApiContext().getProcessingEnv().getTypeUtils().getWildcardType(null, getMirror()));
   }

   @Override
   public Array asArray()
   {
      return MirrorAdapter.getShadow(getApi(), getApi().getJdkApiContext().getProcessingEnv().getTypeUtils().getArrayType(getMirror()));
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

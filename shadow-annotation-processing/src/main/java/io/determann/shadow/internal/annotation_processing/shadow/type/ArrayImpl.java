package io.determann.shadow.internal.annotation_processing.shadow.type;

import io.determann.shadow.api.C;
import io.determann.shadow.api.annotation_processing.AP;
import io.determann.shadow.implementation.support.api.shadow.type.ArraySupport;

import javax.lang.model.type.ArrayType;
import java.util.List;

import static io.determann.shadow.api.annotation_processing.adapter.Adapters.adapt;

public final class ArrayImpl extends TypeImpl<ArrayType> implements AP.Array
{

   public ArrayImpl(AP.Context context, ArrayType arrayType)
   {
      super(context, arrayType);
   }

   @Override
   public boolean isSubtypeOf(C.Type type)
   {
      return adapt(getApi()).toTypes().isSubtype(adapt((AP.Type) type).toTypeMirror(), getMirror());
   }

   @Override
   public AP.Type getComponentType()
   {
      return adapt(getApi(), getMirror().getComponentType());
   }

   @Override
   public List<AP.Type> getDirectSuperTypes()
   {
      return adapt(getApi()).toTypes()
                       .directSupertypes(getMirror())
                       .stream()
                       .map(typeMirror1 -> adapt(getApi(), typeMirror1))
                       .toList();
   }

   @Override
   public AP.Array asArray()
   {
      return adapt(getApi(), adapt(getApi()).toTypes().getArrayType(getMirror()));
   }

   @Override
   public AP.Wildcard asExtendsWildcard()
   {
      return adapt(getApi(), adapt(getApi()).toTypes().getWildcardType(getMirror(), null));
   }

   @Override
   public AP.Wildcard asSuperWildcard()
   {
      return adapt(getApi(), adapt(getApi()).toTypes().getWildcardType(null, getMirror()));
   }

   @Override
   public boolean representsSameType(C.Type type)
   {
      return ArraySupport.representsSameType(this, type);
   }

   @Override
   public AP.Array erasure()
   {
      return adapt(getApi(), ((ArrayType) adapt(getApi()).toTypes().erasure(getMirror())));
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

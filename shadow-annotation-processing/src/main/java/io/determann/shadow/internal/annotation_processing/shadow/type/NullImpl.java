package io.determann.shadow.internal.annotation_processing.shadow.type;

import io.determann.shadow.api.C;
import io.determann.shadow.api.annotation_processing.AP;
import io.determann.shadow.implementation.support.api.shadow.type.NullSupport;

import javax.lang.model.type.NullType;

public class NullImpl extends TypeImpl<NullType> implements AP.Null
{
   public NullImpl(AP.Context LangModelContext, NullType nullType)
   {
      super(LangModelContext, nullType);
   }

   @Override
   public boolean representsSameType(C.Type type)
   {
      return NullSupport.representsSameType(this, type);
   }

   @Override
   public int hashCode()
   {
      return NullSupport.hashCode(this);
   }

   @Override
   public boolean equals(Object other)
   {
      return NullSupport.equals(this, other);
   }

   @Override
   public String toString()
   {
      return NullSupport.toString(this);
   }
}

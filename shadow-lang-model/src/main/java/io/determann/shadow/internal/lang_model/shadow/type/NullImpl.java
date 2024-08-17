package io.determann.shadow.internal.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.shadow.TypeKind;
import io.determann.shadow.api.shadow.type.Null;
import io.determann.shadow.api.shadow.type.Shadow;
import io.determann.shadow.implementation.support.api.shadow.type.NullSupport;

import javax.lang.model.type.NullType;

public class NullImpl extends ShadowImpl<NullType> implements Null
{
   public NullImpl(LangModelContext LangModelContext, NullType nullType)
   {
      super(LangModelContext, nullType);
   }

   @Override
   public boolean representsSameType(Shadow shadow)
   {
      return NullSupport.representsSameType(this, shadow);
   }

   @Override
   public TypeKind getKind()
   {
      return TypeKind.NULL;
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

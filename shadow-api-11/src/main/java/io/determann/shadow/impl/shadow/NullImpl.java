package io.determann.shadow.impl.shadow;

import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.shadow.Null;

import javax.lang.model.type.NullType;
import java.util.Objects;

public class NullImpl extends ShadowImpl<NullType> implements Null
{
   public NullImpl(ShadowApi shadowApi, NullType nullType)
   {
      super(shadowApi, nullType);
   }

   @Override
   public TypeKind getTypeKind()
   {
      return TypeKind.NULL;
   }

   @Override
   public int hashCode()
   {
      return Objects.hashCode(getTypeKind());
   }

   @Override
   public boolean equals(Object other)
   {
      return other != null && getClass().equals(other.getClass());
   }
}

package io.determann.shadow.impl.shadow;

import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.shadow.Void;

import javax.lang.model.type.NoType;
import java.util.Objects;

public class VoidImpl extends ShadowImpl<NoType> implements Void
{
   public VoidImpl(ShadowApi shadowApi, NoType typeMirror)
   {
      super(shadowApi, typeMirror);
   }

   @Override
   public TypeKind getTypeKind()
   {
      return TypeKind.VOID;
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

package io.determann.shadow.internal.reflection.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.shadow.Null;
import io.determann.shadow.api.shadow.Shadow;

import java.util.Objects;

public class NullImpl implements Null
{
   @Override
   public boolean representsSameType(Shadow shadow)
   {
      return shadow != null && shadow.isTypeKind(TypeKind.NULL);
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
      return other instanceof Null;
   }
}

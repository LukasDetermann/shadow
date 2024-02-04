package io.determann.shadow.internal.reflection.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.shadow.Shadow;
import io.determann.shadow.api.shadow.Void;

import java.util.Objects;

public class VoidImpl implements Void
{
   @Override
   public TypeKind getTypeKind()
   {
      return TypeKind.VOID;
   }

   @Override
   public boolean representsSameType(Shadow shadow)
   {
      return shadow != null && shadow.isTypeKind(TypeKind.VOID);
   }

   @Override
   public int hashCode()
   {
      return Objects.hashCode(getTypeKind());
   }

   @Override
   public boolean equals(Object other)
   {
      return other instanceof Void;
   }
}

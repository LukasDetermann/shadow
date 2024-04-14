package io.determann.shadow.internal.reflection.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.shadow.Shadow;
import io.determann.shadow.api.shadow.Void;

import java.util.Objects;

public class VoidImpl implements Void
{
   @Override
   public TypeKind getKind()
   {
      return TypeKind.VOID;
   }

   @Override
   public boolean representsSameType(Shadow shadow)
   {
      return shadow != null && shadow.isKind(TypeKind.VOID);
   }

   @Override
   public int hashCode()
   {
      return Objects.hashCode(getKind());
   }

   @Override
   public boolean equals(Object other)
   {
      return other instanceof Void;
   }
}

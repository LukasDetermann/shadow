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
      return shadow != null && getTypeKind().equals(shadow.getTypeKind());
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

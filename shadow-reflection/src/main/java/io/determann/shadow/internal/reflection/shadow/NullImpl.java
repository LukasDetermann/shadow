package io.determann.shadow.internal.reflection.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.shadow.Null;
import io.determann.shadow.api.shadow.Shadow;

import java.util.Objects;

import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;

public class NullImpl implements Null
{
   @Override
   public boolean representsSameType(Shadow shadow)
   {
      return shadow != null && shadow.isKind(TypeKind.NULL);
   }

   @Override
   public TypeKind getKind()
   {
      return TypeKind.NULL;
   }

   @Override
   public int hashCode()
   {
      return Objects.hashCode(getKind());
   }

   @Override
   public boolean equals(Object other)
   {
      return other instanceof Null;
   }

   @Override
   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
   }
}

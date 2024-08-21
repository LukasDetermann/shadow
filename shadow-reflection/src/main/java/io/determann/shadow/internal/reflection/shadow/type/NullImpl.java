package io.determann.shadow.internal.reflection.shadow.type;

import io.determann.shadow.api.reflection.shadow.type.R_Shadow;
import io.determann.shadow.api.shadow.C_TypeKind;
import io.determann.shadow.api.shadow.type.C_Null;
import io.determann.shadow.api.shadow.type.C_Shadow;
import io.determann.shadow.implementation.support.api.shadow.type.NullSupport;

import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;

public class NullImpl implements C_Null,
                                 R_Shadow
{
   @Override
   public boolean representsSameType(C_Shadow shadow)
   {
      return NullSupport.representsSameType(this, shadow);
   }

   @Override
   public C_TypeKind getKind()
   {
      return C_TypeKind.NULL;
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

   @Override
   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
   }
}

package io.determann.shadow.internal.reflection.shadow.type;

import io.determann.shadow.api.reflection.shadow.type.R_Shadow;
import io.determann.shadow.api.shadow.C_TypeKind;
import io.determann.shadow.api.shadow.type.C_Shadow;
import io.determann.shadow.api.shadow.type.C_Void;
import io.determann.shadow.implementation.support.api.shadow.type.VoidSupport;

import static io.determann.shadow.api.Operations.SHADOW_GET_KIND;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;

public class VoidImpl implements C_Void,
                                 R_Shadow
{
   @Override
   public C_TypeKind getKind()
   {
      return C_TypeKind.VOID;
   }

   @Override
   public boolean representsSameType(C_Shadow shadow)
   {
      return shadow != null && C_TypeKind.VOID.equals(requestOrThrow(shadow, SHADOW_GET_KIND));
   }

   @Override
   public int hashCode()
   {
      return VoidSupport.hashCode(this);
   }

   @Override
   public boolean equals(Object other)
   {
      return VoidSupport.equals(this, other);
   }

   @Override
   public String toString()
   {
      return VoidSupport.toString(this);
   }

   @Override
   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
   }
}

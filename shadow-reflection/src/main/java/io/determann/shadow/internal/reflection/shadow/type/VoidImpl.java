package io.determann.shadow.internal.reflection.shadow.type;

import io.determann.shadow.api.reflection.shadow.type.ShadowReflection;
import io.determann.shadow.api.shadow.TypeKind;
import io.determann.shadow.api.shadow.type.Shadow;
import io.determann.shadow.api.shadow.type.Void;
import io.determann.shadow.implementation.support.api.shadow.type.VoidSupport;

import static io.determann.shadow.api.shadow.Operations.SHADOW_GET_KIND;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;
import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;

public class VoidImpl implements Void,
                                 ShadowReflection
{
   @Override
   public TypeKind getKind()
   {
      return TypeKind.VOID;
   }

   @Override
   public boolean representsSameType(Shadow shadow)
   {
      return shadow != null && TypeKind.VOID.equals(requestOrThrow(shadow, SHADOW_GET_KIND));
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

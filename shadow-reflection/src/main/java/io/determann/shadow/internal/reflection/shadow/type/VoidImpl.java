package io.determann.shadow.internal.reflection.shadow.type;

import io.determann.shadow.api.reflection.shadow.type.R_Type;
import io.determann.shadow.api.shadow.type.C_Type;
import io.determann.shadow.api.shadow.type.C_Void;
import io.determann.shadow.implementation.support.api.shadow.type.VoidSupport;

import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;

public class VoidImpl implements C_Void,
                                 R_Type
{
   @Override
   public boolean representsSameType(C_Type type)
   {
      return type instanceof C_Void;
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

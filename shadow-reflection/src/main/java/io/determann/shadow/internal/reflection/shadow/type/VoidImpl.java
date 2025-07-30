package io.determann.shadow.internal.reflection.shadow.type;

import io.determann.shadow.api.C;
import io.determann.shadow.api.query.Implementation;
import io.determann.shadow.api.reflection.R;
import io.determann.shadow.implementation.support.api.shadow.type.VoidSupport;

import static io.determann.shadow.api.reflection.Adapter.IMPLEMENTATION;

public class VoidImpl implements R.Void
{
   @Override
   public boolean representsSameType(C.Type type)
   {
      return type instanceof C.Void;
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
   public Implementation getImplementation()
   {
      return IMPLEMENTATION;
   }
}

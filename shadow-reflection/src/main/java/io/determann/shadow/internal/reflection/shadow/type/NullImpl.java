package io.determann.shadow.internal.reflection.shadow.type;

import io.determann.shadow.api.C;
import io.determann.shadow.api.query.Implementation;
import io.determann.shadow.api.reflection.R;
import io.determann.shadow.implementation.support.api.shadow.type.NullSupport;

import static io.determann.shadow.api.reflection.Adapter.IMPLEMENTATION;

public class NullImpl implements R.Null
{
   @Override
   public boolean representsSameType(C.Type type)
   {
      return NullSupport.representsSameType(this, type);
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
   public Implementation getImplementation()
   {
      return IMPLEMENTATION;
   }
}

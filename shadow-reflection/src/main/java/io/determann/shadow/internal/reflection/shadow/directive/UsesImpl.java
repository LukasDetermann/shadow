package io.determann.shadow.internal.reflection.shadow.directive;

import io.determann.shadow.api.query.Implementation;
import io.determann.shadow.api.reflection.Adapter;
import io.determann.shadow.api.reflection.R;
import io.determann.shadow.implementation.support.api.shadow.directive.UsesSupport;

import static io.determann.shadow.api.reflection.Adapter.IMPLEMENTATION;

public class UsesImpl implements R.Uses
{
   private final String usesDirective;

   public UsesImpl(String usesDirective)
   {
      this.usesDirective = usesDirective;
   }

   @Override
   public R.Declared getService()
   {
      return Adapter.getDeclared(usesDirective).orElseThrow();
   }

   @Override
   public Implementation getImplementation()
   {
      return IMPLEMENTATION;
   }

   @Override
   public boolean equals(Object other)
   {
      return UsesSupport.equals(this, other);
   }

   @Override
   public int hashCode()
   {
      return UsesSupport.hashCode(this);
   }

   @Override
   public String toString()
   {
      return UsesSupport.toString(this);
   }

   public String getReflection()
   {
      return usesDirective;
   }
}

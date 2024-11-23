package io.determann.shadow.internal.reflection.shadow.directive;

import io.determann.shadow.api.Implementation;
import io.determann.shadow.api.reflection.R_Adapter;
import io.determann.shadow.api.reflection.shadow.directive.R_Uses;
import io.determann.shadow.api.reflection.shadow.type.R_Declared;
import io.determann.shadow.implementation.support.api.shadow.directive.UsesSupport;

import static io.determann.shadow.api.reflection.R_Adapter.IMPLEMENTATION;

public class UsesImpl implements R_Uses
{
   private final String usesDirective;

   public UsesImpl(String usesDirective)
   {
      this.usesDirective = usesDirective;
   }

   @Override
   public R_Declared getService()
   {
      return R_Adapter.getDeclared(usesDirective).orElseThrow();
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

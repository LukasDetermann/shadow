package io.determann.shadow.internal.reflection.shadow.directive;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.reflection.shadow.directive.UsesReflection;
import io.determann.shadow.api.reflection.shadow.type.DeclaredReflection;
import io.determann.shadow.implementation.support.api.shadow.directive.UsesSupport;

import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;

public class UsesImpl implements UsesReflection
{
   private final String usesDirective;

   public UsesImpl(String usesDirective)
   {
      this.usesDirective = usesDirective;
   }

   @Override
   public DeclaredReflection getService()
   {
      return ReflectionAdapter.getDeclared(usesDirective).orElseThrow();
   }

   @Override
   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
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

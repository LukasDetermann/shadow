package io.determann.shadow.impl.reflection.shadow.module;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.shadow.Declared;
import io.determann.shadow.api.shadow.module.DirectiveKind;
import io.determann.shadow.api.shadow.module.Uses;

import java.util.Objects;

public class UsesImpl implements Uses
{
   private final String usesDirective;

   public UsesImpl(String usesDirective)
   {
      this.usesDirective = usesDirective;
   }

   @Override
   public Declared getService()
   {
      return ReflectionAdapter.getDeclaredShadow(usesDirective).orElseThrow();
   }

   @Override
   public DirectiveKind getKind()
   {
      return DirectiveKind.USES;
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (other == null || !getClass().equals(other.getClass()))
      {
         return false;
      }
      UsesImpl otherUses = (UsesImpl) other;
      return Objects.equals(getKind(), otherUses.getKind()) &&
             Objects.equals(getService(), otherUses.getService());
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getKind(), getService());
   }

   @Override
   public String toString()
   {
      return usesDirective;
   }

   public String getReflection()
   {
      return usesDirective;
   }
}

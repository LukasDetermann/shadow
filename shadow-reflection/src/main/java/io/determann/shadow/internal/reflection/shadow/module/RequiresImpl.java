package io.determann.shadow.internal.reflection.shadow.module;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.shadow.module.Requires;
import io.determann.shadow.api.shadow.structure.Module;

import java.lang.module.ModuleDescriptor;
import java.util.Objects;

public class RequiresImpl implements Requires
{

   private final ModuleDescriptor.Requires requiresDirective;

   public RequiresImpl(ModuleDescriptor.Requires requiresDirective)
   {
      this.requiresDirective = requiresDirective;
   }

   @Override
   public boolean isStatic()
   {
      return  requiresDirective.modifiers().contains(ModuleDescriptor.Requires.Modifier.STATIC);
   }

   @Override
   public boolean isTransitive()
   {
      return  requiresDirective.modifiers().contains(ModuleDescriptor.Requires.Modifier.TRANSITIVE);
   }

   @Override
   public Module getDependency()
   {
      return ReflectionAdapter.getModuleShadow(requiresDirective.name());
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
      RequiresImpl otherRequires = (RequiresImpl) other;
      return Objects.equals(isStatic(), otherRequires.isStatic()) &&
             Objects.equals(isTransitive(), otherRequires.isTransitive()) &&
             Objects.equals(getDependency(), otherRequires.getDependency());
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getDependency(), isStatic(), isTransitive());
   }

   @Override
   public String toString()
   {
      return requiresDirective.toString();
   }

   public ModuleDescriptor.Requires getReflection()
   {
      return requiresDirective;
   }
}

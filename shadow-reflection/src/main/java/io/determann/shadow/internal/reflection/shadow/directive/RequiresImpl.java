package io.determann.shadow.internal.reflection.shadow.directive;

import io.determann.shadow.api.reflection.R_Adapter;
import io.determann.shadow.api.reflection.shadow.directive.R_Requires;
import io.determann.shadow.api.reflection.shadow.structure.R_Module;
import io.determann.shadow.implementation.support.api.shadow.directive.RequiresSupport;

import java.lang.module.ModuleDescriptor;

import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;

public class RequiresImpl implements R_Requires
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
   public R_Module getDependency()
   {
      return R_Adapter.getModuleShadow(requiresDirective.name());
   }

   @Override
   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
   }

   @Override
   public boolean equals(Object other)
   {
      return RequiresSupport.equals(this, other);
   }

   @Override
   public int hashCode()
   {
      return RequiresSupport.hashCode(this);
   }

   @Override
   public String toString()
   {
      return RequiresSupport.toString(this);
   }

   public ModuleDescriptor.Requires getReflection()
   {
      return requiresDirective;
   }
}

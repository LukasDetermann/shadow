package io.determann.shadow.internal.reflection.shadow.directive;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.reflection.shadow.directive.OpensReflection;
import io.determann.shadow.api.shadow.structure.Module;
import io.determann.shadow.api.shadow.structure.Package;
import io.determann.shadow.implementation.support.api.shadow.directive.OpensSupport;

import java.lang.module.ModuleDescriptor;
import java.util.List;

import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;

public class OpensImpl implements OpensReflection
{
   private final ModuleDescriptor.Opens opensDirective;

   public OpensImpl(ModuleDescriptor.Opens opensDirective)
   {
      this.opensDirective = opensDirective;
   }

   @Override
   public Package getPackage()
   {
      return ReflectionAdapter.getPackage(opensDirective.source());
   }

   @Override
   public List<Module> getTargetModules()
   {
      return opensDirective.targets()
                           .stream()
                           .map(ReflectionAdapter::getModuleShadow)
                           .toList();
   }

   @Override
   public boolean toAll()
   {
      return getTargetModules().isEmpty();
   }

   @Override
   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
   }

   @Override
   public boolean equals(Object other)
   {
      return OpensSupport.equals(this, other);
   }

   @Override
   public int hashCode()
   {
      return OpensSupport.hashCode(this);
   }

   @Override
   public String toString()
   {
      return OpensSupport.toString(this);
   }

   public ModuleDescriptor.Opens getReflection()
   {
      return opensDirective;
   }
}

package io.determann.shadow.internal.reflection.shadow.directive;

import io.determann.shadow.api.Implementation;
import io.determann.shadow.api.reflection.R_Adapter;
import io.determann.shadow.api.reflection.shadow.directive.R_Opens;
import io.determann.shadow.api.reflection.shadow.structure.R_Module;
import io.determann.shadow.api.reflection.shadow.structure.R_Package;
import io.determann.shadow.implementation.support.api.shadow.directive.OpensSupport;

import java.lang.module.ModuleDescriptor;
import java.util.List;

import static io.determann.shadow.api.reflection.R_Adapter.IMPLEMENTATION;

public class OpensImpl implements R_Opens
{
   private final ModuleDescriptor.Opens opensDirective;

   public OpensImpl(ModuleDescriptor.Opens opensDirective)
   {
      this.opensDirective = opensDirective;
   }

   @Override
   public R_Package getPackage()
   {
      return R_Adapter.getPackage(opensDirective.source());
   }

   @Override
   public List<R_Module> getTargetModules()
   {
      return opensDirective.targets()
                           .stream()
                           .map(R_Adapter::getModuleType)
                           .toList();
   }

   @Override
   public boolean toAll()
   {
      return getTargetModules().isEmpty();
   }

   @Override
   public Implementation getImplementation()
   {
      return IMPLEMENTATION;
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

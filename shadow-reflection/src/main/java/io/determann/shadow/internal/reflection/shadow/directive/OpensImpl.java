package io.determann.shadow.internal.reflection.shadow.directive;

import io.determann.shadow.api.query.Implementation;
import io.determann.shadow.api.reflection.Adapter;
import io.determann.shadow.api.reflection.R;
import io.determann.shadow.implementation.support.api.shadow.directive.OpensSupport;

import java.lang.module.ModuleDescriptor;
import java.util.List;

import static io.determann.shadow.api.reflection.Adapter.IMPLEMENTATION;

public class OpensImpl implements R.Opens
{
   private final ModuleDescriptor.Opens opensDirective;

   public OpensImpl(ModuleDescriptor.Opens opensDirective)
   {
      this.opensDirective = opensDirective;
   }

   @Override
   public R.Package getPackage()
   {
      return Adapter.getPackage(opensDirective.source());
   }

   @Override
   public List<R.Module> getTargetModules()
   {
      return opensDirective.targets()
                           .stream()
                           .map(Adapter::getModuleType)
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

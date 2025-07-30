package io.determann.shadow.internal.reflection.shadow.directive;

import io.determann.shadow.api.query.Implementation;
import io.determann.shadow.api.reflection.Adapter;
import io.determann.shadow.api.reflection.R;
import io.determann.shadow.implementation.support.api.shadow.directive.ExportsSupport;

import java.lang.module.ModuleDescriptor;
import java.util.Collections;
import java.util.List;

import static io.determann.shadow.api.reflection.Adapter.IMPLEMENTATION;

public class ExportsImpl implements R.Exports
{
   private final ModuleDescriptor.Exports exportsDirective;

   public ExportsImpl(ModuleDescriptor.Exports exportsDirective)
   {
      this.exportsDirective = exportsDirective;
   }

   @Override
   public R.Package getPackage()
   {
      return Adapter.getPackage(exportsDirective.source());
   }

   @Override
   public List<R.Module> getTargetModules()
   {
      return exportsDirective.targets() == null ?
             Collections.emptyList() :
             exportsDirective.targets()
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
      return ExportsSupport.equals(this, other);
   }

   @Override
   public int hashCode()
   {
      return ExportsSupport.hashCode(this);
   }

   @Override
   public String toString()
   {
      return ExportsSupport.toString(this);
   }

   public ModuleDescriptor.Exports getReflection()
   {
      return exportsDirective;
   }
}

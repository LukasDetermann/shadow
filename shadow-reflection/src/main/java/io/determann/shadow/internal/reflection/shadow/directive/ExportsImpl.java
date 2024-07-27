package io.determann.shadow.internal.reflection.shadow.directive;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.reflection.shadow.directive.ExportsReflection;
import io.determann.shadow.api.shadow.structure.Module;
import io.determann.shadow.api.shadow.structure.Package;
import io.determann.shadow.implementation.support.api.shadow.directive.ExportsSupport;

import java.lang.module.ModuleDescriptor;
import java.util.Collections;
import java.util.List;

import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;

public class ExportsImpl implements ExportsReflection
{
   private final ModuleDescriptor.Exports exportsDirective;

   public ExportsImpl(ModuleDescriptor.Exports exportsDirective)
   {
      this.exportsDirective = exportsDirective;
   }

   @Override
   public Package getPackage()
   {
      return ReflectionAdapter.getPackage(exportsDirective.source());
   }

   @Override
   public List<Module> getTargetModules()
   {
      return exportsDirective.targets() == null ?
             Collections.emptyList() :
             exportsDirective.targets()
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

package io.determann.shadow.internal.reflection.shadow.directive;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.shadow.directive.Exports;
import io.determann.shadow.api.shadow.structure.Module;
import io.determann.shadow.api.shadow.structure.Package;

import java.lang.module.ModuleDescriptor;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ExportsImpl implements Exports
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
      ExportsImpl otherExports = (ExportsImpl) other;
      return Objects.equals(getTargetModules(), otherExports.getTargetModules()) &&
             Objects.equals(getPackage(), otherExports.getPackage());
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getPackage(), getTargetModules());
   }

   @Override
   public String toString()
   {
      return exportsDirective.toString();
   }

   public ModuleDescriptor.Exports getReflection()
   {
      return exportsDirective;
   }
}

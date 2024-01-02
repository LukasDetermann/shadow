package io.determann.shadow.impl.reflection.shadow.module;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.module.DirectiveKind;
import io.determann.shadow.api.shadow.module.Exports;

import java.lang.module.ModuleDescriptor;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
      return ReflectionAdapter.getPackageShadow(exportsDirective.source());
   }

   @Override
   public List<Module> getTargetModules()
   {
      return exportsDirective.targets() == null ?
             Collections.emptyList() :
             exportsDirective.targets()
                             .stream()
                             .map(ReflectionAdapter::getModuleShadow)
                             .collect(Collectors.toUnmodifiableList());
   }

   @Override
   public boolean toAll()
   {
      return getTargetModules().isEmpty();
   }

   @Override
   public DirectiveKind getKind()
   {
      return DirectiveKind.EXPORTS;
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
      return Objects.equals(getKind(), otherExports.getKind()) &&
             Objects.equals(getTargetModules(), otherExports.getTargetModules()) &&
             Objects.equals(getPackage(), otherExports.getPackage());
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getKind(), getPackage(), getTargetModules());
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

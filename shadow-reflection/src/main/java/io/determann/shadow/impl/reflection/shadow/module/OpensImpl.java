package io.determann.shadow.impl.reflection.shadow.module;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.module.DirectiveKind;
import io.determann.shadow.api.shadow.module.Opens;

import java.lang.module.ModuleDescriptor;
import java.util.List;
import java.util.Objects;

public class OpensImpl implements Opens
{
   private final ModuleDescriptor.Opens opensDirective;

   public OpensImpl(ModuleDescriptor.Opens opensDirective)
   {
      this.opensDirective = opensDirective;
   }

   @Override
   public Package getPackage()
   {
      return ReflectionAdapter.getPackageShadow(opensDirective.source());
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
   public DirectiveKind getKind()
   {
      return DirectiveKind.OPENS;
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
      OpensImpl otherOpens = (OpensImpl) other;
      return Objects.equals(getKind(), otherOpens.getKind()) &&
             Objects.equals(getTargetModules(), otherOpens.getTargetModules()) &&
             Objects.equals(getPackage(), otherOpens.getPackage());
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getKind(), getPackage(), getTargetModules());
   }

   @Override
   public String toString()
   {
      return opensDirective.toString();
   }

   public ModuleDescriptor.Opens getReflection()
   {
      return opensDirective;
   }
}

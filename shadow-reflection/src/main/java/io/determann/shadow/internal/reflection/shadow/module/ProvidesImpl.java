package io.determann.shadow.internal.reflection.shadow.module;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.shadow.module.Provides;
import io.determann.shadow.api.shadow.type.Declared;

import java.lang.module.ModuleDescriptor;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ProvidesImpl implements Provides
{
   private final ModuleDescriptor.Provides providesDirective;

   public ProvidesImpl(ModuleDescriptor.Provides providesDirective)
   {
      this.providesDirective = providesDirective;
   }

   @Override
   public Declared getService()
   {
      return ReflectionAdapter.getDeclared(providesDirective.service()).orElseThrow();
   }

   @Override
   public List<Declared> getImplementations()
   {
      return providesDirective.providers()
                              .stream()
                              .map(ReflectionAdapter::getDeclared)
                              .map(Optional::orElseThrow)
                              .toList();
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
      ProvidesImpl otherProvides = (ProvidesImpl) other;
      return Objects.equals(getImplementations(), otherProvides.getImplementations()) &&
             Objects.equals(getService(), otherProvides.getService());
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getService(), getImplementations());
   }

   @Override
   public String toString()
   {
      return providesDirective.toString();
   }

   public ModuleDescriptor.Provides getReflection()
   {
      return providesDirective;
   }
}

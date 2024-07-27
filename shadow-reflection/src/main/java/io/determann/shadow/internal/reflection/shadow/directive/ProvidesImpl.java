package io.determann.shadow.internal.reflection.shadow.directive;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.reflection.shadow.directive.ProvidesReflection;
import io.determann.shadow.api.shadow.type.Declared;
import io.determann.shadow.implementation.support.api.shadow.directive.ProvidesSupport;

import java.lang.module.ModuleDescriptor;
import java.util.List;
import java.util.Optional;

import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;

public class ProvidesImpl implements ProvidesReflection
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
   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
   }

   @Override
   public boolean equals(Object other)
   {
      return ProvidesSupport.equals(this, other);
   }

   @Override
   public int hashCode()
   {
      return ProvidesSupport.hashCode(this);
   }

   @Override
   public String toString()
   {
      return ProvidesSupport.toString(this);
   }

   public ModuleDescriptor.Provides getReflection()
   {
      return providesDirective;
   }
}

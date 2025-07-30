package io.determann.shadow.internal.reflection.shadow.directive;

import io.determann.shadow.api.query.Implementation;
import io.determann.shadow.api.reflection.Adapter;
import io.determann.shadow.api.reflection.R;
import io.determann.shadow.implementation.support.api.shadow.directive.ProvidesSupport;

import java.lang.module.ModuleDescriptor;
import java.util.List;
import java.util.Optional;

import static io.determann.shadow.api.reflection.Adapter.IMPLEMENTATION;

public class ProvidesImpl implements R.Provides
{
   private final ModuleDescriptor.Provides providesDirective;

   public ProvidesImpl(ModuleDescriptor.Provides providesDirective)
   {
      this.providesDirective = providesDirective;
   }

   @Override
   public R.Declared getService()
   {
      return Adapter.getDeclared(providesDirective.service()).orElseThrow();
   }

   @Override
   public List<R.Declared> getImplementations()
   {
      return providesDirective.providers()
                              .stream()
                              .map(Adapter::getDeclared)
                              .map(Optional::orElseThrow)
                              .toList();
   }

   @Override
   public Implementation getImplementation()
   {
      return IMPLEMENTATION;
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

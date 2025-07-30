package io.determann.shadow.internal.reflection.shadow.structure;

import io.determann.shadow.api.C;
import io.determann.shadow.api.query.Implementation;
import io.determann.shadow.api.reflection.Adapter;
import io.determann.shadow.api.reflection.R;
import io.determann.shadow.internal.reflection.NamedSupplier;

import java.lang.module.ModuleDescriptor;
import java.util.*;
import java.util.stream.Collectors;

import static io.determann.shadow.api.query.Operations.QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME;
import static io.determann.shadow.api.query.Provider.requestOrThrow;
import static io.determann.shadow.api.reflection.Adapter.IMPLEMENTATION;
import static java.util.Collections.unmodifiableList;


public class ModuleImpl implements R.Module
{
   private final List<R.AnnotationUsage> annotationUsages;
   private final NamedSupplier<ModuleDescriptor> moduleDescriptorSupplier;

   public ModuleImpl(NamedSupplier<ModuleDescriptor> moduleDescriptorSupplier)
   {
      this(moduleDescriptorSupplier, Collections.emptyList());
   }

   public ModuleImpl(NamedSupplier<ModuleDescriptor> moduleDescriptorSupplier, List<R.AnnotationUsage> annotationUsages)
   {
      this.moduleDescriptorSupplier = moduleDescriptorSupplier;
      this.annotationUsages = annotationUsages;
   }

   @Override
   public String getName()
   {
      String qualifiedName = getQualifiedName();
      int index = qualifiedName.lastIndexOf(".");
      if (index == -1)
      {
         return qualifiedName;
      }
      return qualifiedName.substring(index + 1);
   }

   @Override
   public List<R.AnnotationUsage> getAnnotationUsages()
   {
      return annotationUsages;
   }

   @Override
   public List<R.AnnotationUsage> getDirectAnnotationUsages()
   {
      return annotationUsages;
   }

   @Override
   public List<R.Declared> getDeclared()
   {
      throw new UnsupportedOperationException("not implemented for reflection");
   }

   @Override
   public Optional<R.Declared> getDeclared(String qualifiedName)
   {
      throw new UnsupportedOperationException("not implemented for reflection");
   }

   @Override
   public String getQualifiedName()
   {
      return Optional.ofNullable(moduleDescriptorSupplier.getName()).orElse("");
   }

   @Override
   public List<R.Package> getPackages()
   {
      return getModuleDescriptor().packages().stream().map(Adapter::getPackage).map(R.Package.class::cast).toList();
   }

   @Override
   public boolean isOpen()
   {
      return getModuleDescriptor().isOpen();
   }

   @Override
   public boolean isUnnamed()
   {
      return getQualifiedName().isEmpty();
   }

   @Override
   public boolean isAutomatic()
   {
      return getModuleDescriptor().isAutomatic();
   }

   @Override
   public List<R.Directive> getDirectives()
   {
      ModuleDescriptor descriptor = getModuleDescriptor();
      List<R.Directive> result = descriptor.requires()
                                           .stream()
                                           .map(Adapter::generalize)
                                           .collect(Collectors.toCollection(ArrayList::new));
      result.addAll(descriptor.exports()
                              .stream()
                              .map(Adapter::generalize)
                              .toList());
      result.addAll(descriptor.opens()
                              .stream()
                              .map(Adapter::generalize)
                              .toList());
      result.addAll(descriptor.uses()
                              .stream()
                              .map(Adapter::getUsesType)
                              .toList());
      result.addAll(descriptor.provides()
                              .stream()
                              .map(Adapter::generalize)
                              .toList());
      return unmodifiableList(result);
   }

   public ModuleDescriptor getModuleDescriptor()
   {
      return moduleDescriptorSupplier.getInstance();
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getQualifiedName());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof C.Module otherModule))
      {
         return false;
      }
      return Objects.equals(getQualifiedName(), requestOrThrow(otherModule, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME));
   }

   public ModuleDescriptor getReflection()
   {
      return getModuleDescriptor();
   }


   @Override
   public Implementation getImplementation()
   {
      return IMPLEMENTATION;
   }
}

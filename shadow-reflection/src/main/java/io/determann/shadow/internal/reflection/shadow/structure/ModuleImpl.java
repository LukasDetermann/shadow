package io.determann.shadow.internal.reflection.shadow.structure;

import io.determann.shadow.api.Implementation;
import io.determann.shadow.api.reflection.R_Adapter;
import io.determann.shadow.api.reflection.shadow.R_AnnotationUsage;
import io.determann.shadow.api.reflection.shadow.directive.R_Directive;
import io.determann.shadow.api.reflection.shadow.structure.R_Module;
import io.determann.shadow.api.reflection.shadow.structure.R_Package;
import io.determann.shadow.api.reflection.shadow.type.R_Declared;
import io.determann.shadow.api.shadow.structure.C_Module;
import io.determann.shadow.api.shadow.type.C_Type;
import io.determann.shadow.internal.reflection.NamedSupplier;

import java.lang.module.ModuleDescriptor;
import java.util.*;
import java.util.stream.Collectors;

import static io.determann.shadow.api.Operations.QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.reflection.R_Adapter.IMPLEMENTATION;
import static java.util.Collections.unmodifiableList;


public class ModuleImpl implements R_Module
{
   private final List<R_AnnotationUsage> annotationUsages;
   private final NamedSupplier<ModuleDescriptor> moduleDescriptorSupplier;

   public ModuleImpl(NamedSupplier<ModuleDescriptor> moduleDescriptorSupplier)
   {
      this(moduleDescriptorSupplier, Collections.emptyList());
   }

   public ModuleImpl(NamedSupplier<ModuleDescriptor> moduleDescriptorSupplier, List<R_AnnotationUsage> annotationUsages)
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
   public List<R_AnnotationUsage> getAnnotationUsages()
   {
      return annotationUsages;
   }

   @Override
   public List<R_AnnotationUsage> getDirectAnnotationUsages()
   {
      return annotationUsages;
   }

   @Override
   public List<R_Declared> getDeclared()
   {
      throw new UnsupportedOperationException("not implemented for reflection");
   }

   @Override
   public Optional<R_Declared> getDeclared(String qualifiedName)
   {
      throw new UnsupportedOperationException("not implemented for reflection");
   }

   @Override
   public String getQualifiedName()
   {
      return Optional.ofNullable(moduleDescriptorSupplier.getName()).orElse("");
   }

   @Override
   public List<R_Package> getPackages()
   {
      return getModuleDescriptor().packages().stream().map(R_Adapter::getPackage).map(R_Package.class::cast).toList();
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
   public List<R_Directive> getDirectives()
   {
      ModuleDescriptor descriptor = getModuleDescriptor();
      List<R_Directive> result = descriptor.requires()
                                           .stream()
                                           .map(R_Adapter::generalize)
                                           .collect(Collectors.toCollection(ArrayList::new));
      result.addAll(descriptor.exports()
                              .stream()
                              .map(R_Adapter::generalize)
                              .toList());
      result.addAll(descriptor.opens()
                              .stream()
                              .map(R_Adapter::generalize)
                              .toList());
      result.addAll(descriptor.uses()
                              .stream()
                              .map(R_Adapter::getUsesType)
                              .toList());
      result.addAll(descriptor.provides()
                              .stream()
                              .map(R_Adapter::generalize)
                              .toList());
      return unmodifiableList(result);
   }

   public boolean representsSameType(C_Type type)
   {
      return equals(type);
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
      if (!(other instanceof C_Module otherModule))
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

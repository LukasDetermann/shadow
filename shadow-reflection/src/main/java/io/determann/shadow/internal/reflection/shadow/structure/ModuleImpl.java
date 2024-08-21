package io.determann.shadow.internal.reflection.shadow.structure;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.reflection.shadow.AnnotationUsageReflection;
import io.determann.shadow.api.reflection.shadow.directive.DirectiveReflection;
import io.determann.shadow.api.reflection.shadow.structure.ModuleReflection;
import io.determann.shadow.api.reflection.shadow.structure.PackageReflection;
import io.determann.shadow.api.reflection.shadow.type.DeclaredReflection;
import io.determann.shadow.api.shadow.TypeKind;
import io.determann.shadow.api.shadow.structure.Module;
import io.determann.shadow.api.shadow.type.Shadow;
import io.determann.shadow.internal.reflection.NamedSupplier;

import java.lang.module.ModuleDescriptor;
import java.util.*;
import java.util.stream.Collectors;

import static io.determann.shadow.api.Operations.QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;
import static java.util.Collections.unmodifiableList;


public class ModuleImpl implements ModuleReflection
{
   private final List<AnnotationUsageReflection> annotationUsages;
   private final NamedSupplier<ModuleDescriptor> moduleDescriptorSupplier;

   public ModuleImpl(NamedSupplier<ModuleDescriptor> moduleDescriptorSupplier)
   {
      this(moduleDescriptorSupplier, Collections.emptyList());
   }

   public ModuleImpl(NamedSupplier<ModuleDescriptor> moduleDescriptorSupplier, List<AnnotationUsageReflection> annotationUsages)
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
   public List<AnnotationUsageReflection> getAnnotationUsages()
   {
      return annotationUsages;
   }

   @Override
   public List<AnnotationUsageReflection> getDirectAnnotationUsages()
   {
      return annotationUsages;
   }

   @Override
   public List<DeclaredReflection> getDeclared()
   {
      throw new UnsupportedOperationException("not implemented for reflection");
   }

   @Override
   public Optional<DeclaredReflection> getDeclared(String qualifiedName)
   {
      throw new UnsupportedOperationException("not implemented for reflection");
   }

   @Override
   public String getQualifiedName()
   {
      return Optional.ofNullable(moduleDescriptorSupplier.getName()).orElse("");
   }

   @Override
   public List<PackageReflection> getPackages()
   {
      return getModuleDescriptor().packages().stream().map(ReflectionAdapter::getPackage).map(PackageReflection.class::cast).toList();
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
   public List<DirectiveReflection> getDirectives()
   {
      ModuleDescriptor descriptor = getModuleDescriptor();
      List<DirectiveReflection> result = descriptor.requires()
                                            .stream()
                                            .map(ReflectionAdapter::generalize)
                                            .collect(Collectors.toCollection(ArrayList::new));
      result.addAll(descriptor.exports()
                              .stream()
                              .map(ReflectionAdapter::generalize)
                              .toList());
      result.addAll(descriptor.opens()
                              .stream()
                              .map(ReflectionAdapter::generalize)
                              .toList());
      result.addAll(descriptor.uses()
                              .stream()
                              .map(ReflectionAdapter::getUsesShadow)
                              .toList());
      result.addAll(descriptor.provides()
                              .stream()
                              .map(ReflectionAdapter::generalize)
                              .toList());
      return unmodifiableList(result);
   }

   @Override
   public TypeKind getKind()
   {
      return TypeKind.MODULE;
   }

   @Override
   public boolean representsSameType(Shadow shadow)
   {
      return equals(shadow);
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
      if (!(other instanceof Module otherModule))
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
   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
   }
}

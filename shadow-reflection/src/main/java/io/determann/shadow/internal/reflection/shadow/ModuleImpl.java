package io.determann.shadow.internal.reflection.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.reflection.query.NameableReflection;
import io.determann.shadow.api.reflection.query.ShadowReflection;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.*;
import io.determann.shadow.api.shadow.module.Directive;
import io.determann.shadow.internal.reflection.NamedSupplier;

import java.lang.module.ModuleDescriptor;
import java.util.*;
import java.util.stream.Collectors;

import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;
import static java.util.Collections.unmodifiableList;


public class ModuleImpl implements Module,
                                   NameableReflection,
                                   ShadowReflection
{
   private final List<AnnotationUsage> annotationUsages;
   private final NamedSupplier<ModuleDescriptor> moduleDescriptorSupplier;

   public ModuleImpl(NamedSupplier<ModuleDescriptor> moduleDescriptorSupplier)
   {
      this(moduleDescriptorSupplier, Collections.emptyList());
   }

   public ModuleImpl(NamedSupplier<ModuleDescriptor> moduleDescriptorSupplier, List<AnnotationUsage> annotationUsages)
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
   public String getJavaDoc()
   {
      return null;
   }

   @Override
   public List<AnnotationUsage> getAnnotationUsages()
   {
      return annotationUsages;
   }

   @Override
   public List<AnnotationUsage> getDirectAnnotationUsages()
   {
      return annotationUsages;
   }

   @Override
   public List<Declared> getDeclared()
   {
      throw new UnsupportedOperationException("not implemented for reflection");
   }

   @Override
   public Optional<Declared> getDeclared(String qualifiedName)
   {
      throw new UnsupportedOperationException("not implemented for reflection");
   }

   @Override
   public String getQualifiedName()
   {
      return Optional.ofNullable(moduleDescriptorSupplier.getName()).orElse("");
   }

   @Override
   public List<Package> getPackages()
   {
      return getModuleDescriptor().packages().stream().map(ReflectionAdapter::getPackage).map(Package.class::cast).toList();
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
   public List<Directive> getDirectives()
   {
      ModuleDescriptor descriptor = getModuleDescriptor();
      List<Directive> result = descriptor.requires()
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
      return Objects.equals(getQualifiedName(), otherModule.getQualifiedName());
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

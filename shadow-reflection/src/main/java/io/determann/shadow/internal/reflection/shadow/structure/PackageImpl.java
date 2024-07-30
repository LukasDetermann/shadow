package io.determann.shadow.internal.reflection.shadow.structure;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.reflection.shadow.structure.PackageReflection;
import io.determann.shadow.api.shadow.annotationusage.AnnotationUsage;
import io.determann.shadow.api.shadow.structure.Module;
import io.determann.shadow.api.shadow.structure.Package;
import io.determann.shadow.api.shadow.type.Declared;
import io.determann.shadow.internal.reflection.NamedSupplier;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static io.determann.shadow.api.shadow.Operations.MODULE_ENCLOSED_GET_MODULE;
import static io.determann.shadow.api.shadow.Operations.QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;
import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;

public class PackageImpl implements PackageReflection
{
   private final NamedSupplier<java.lang.Package> packageSupplier;

   public PackageImpl(NamedSupplier<java.lang.Package> packageSupplier)
   {
      this.packageSupplier = packageSupplier;
   }

   @Override
   public Module getModule()
   {
      throw new UnsupportedOperationException("not implemented for reflection");
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
   public List<AnnotationUsage> getAnnotationUsages()
   {
      return Arrays.stream(packageSupplier.getInstance().getAnnotations())
                   .map(ReflectionAdapter::generalize)
                   .toList();
   }

   @Override
   public List<AnnotationUsage> getDirectAnnotationUsages()
   {
      return Arrays.stream(packageSupplier.getInstance().getDeclaredAnnotations())
                   .map(ReflectionAdapter::generalize)
                   .toList();
   }

   @Override
   public List<Declared> getDeclared()
   {
      return getContent();
   }

   @Override
   public Optional<Declared> getDeclared(String qualifiedName)
   {
      throw new UnsupportedOperationException("not implemented for reflection");
   }

   @Override
   public String getQualifiedName()
   {
      return packageSupplier.getName();
   }

   @Override
   public List<Declared> getContent()
   {
      throw new UnsupportedOperationException("not implemented for reflection");
   }

   @Override
   public boolean isUnnamed()
   {
      return getQualifiedName().isEmpty();
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getQualifiedName(), getModule());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof Package otherPackage))
      {
         return false;
      }
      return Objects.equals(getQualifiedName(), requestOrThrow(otherPackage, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME)) &&
             Objects.equals(getModule(), requestOrThrow(otherPackage, MODULE_ENCLOSED_GET_MODULE));
   }

   public java.lang.Package getReflection()
   {
      return packageSupplier.getInstance();
   }

   @Override
   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
   }
}

package io.determann.shadow.internal.reflection.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.reflection.query.NameableReflection;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.*;
import io.determann.shadow.internal.reflection.NamedSupplier;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;

public class PackageImpl implements Package,
                                    NameableReflection
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
   public String getJavaDoc()
   {
      return null;
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
   public TypeKind getKind()
   {
      return TypeKind.PACKAGE;
   }

   @Override
   public boolean representsSameType(Shadow shadow)
   {
      return equals(shadow);
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
      return Objects.equals(getQualifiedName(), otherPackage.getQualifiedName()) &&
             Objects.equals(getModule(), otherPackage.getModule());
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

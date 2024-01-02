package io.determann.shadow.impl.reflection.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.Declared;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.Shadow;
import io.determann.shadow.impl.reflection.NamedSupplier;

import java.util.*;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public class PackageImpl implements Package
{
   private final NamedSupplier<java.lang.Package> packageSupplier;

   public PackageImpl(NamedSupplier<java.lang.Package> packageSupplier)
   {
      this.packageSupplier = packageSupplier;
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
                   .map(ReflectionAdapter::getAnnotationUsage)
                   .collect(collectingAndThen(toList(), Collections::unmodifiableList));
   }

   @Override
   public List<AnnotationUsage> getDirectAnnotationUsages()
   {
      return Arrays.stream(packageSupplier.getInstance().getDeclaredAnnotations())
                   .map(ReflectionAdapter::getAnnotationUsage)
                   .collect(collectingAndThen(toList(), Collections::unmodifiableList));
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
   public TypeKind getTypeKind()
   {
      return TypeKind.PACKAGE;
   }

   @Override
   public boolean representsSameType(Shadow shadow)
   {
      return shadow != null && getTypeKind().equals(shadow.getTypeKind());
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
      if (!(other instanceof Package))
      {
         return false;
      }
      Package otherPackage = (Package) other;
      return Objects.equals(getQualifiedName(), otherPackage.getQualifiedName());
   }

   public java.lang.Package getReflection()
   {
      return packageSupplier.getInstance();
   }
}

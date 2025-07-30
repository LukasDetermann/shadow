package io.determann.shadow.internal.reflection.shadow.structure;

import io.determann.shadow.api.C;
import io.determann.shadow.api.query.Implementation;
import io.determann.shadow.api.reflection.Adapter;
import io.determann.shadow.api.reflection.R;
import io.determann.shadow.internal.reflection.NamedSupplier;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static io.determann.shadow.api.query.Operations.MODULE_ENCLOSED_GET_MODULE;
import static io.determann.shadow.api.query.Operations.QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME;
import static io.determann.shadow.api.query.Provider.requestOrThrow;
import static io.determann.shadow.api.reflection.Adapter.IMPLEMENTATION;

public class PackageImpl implements R.Package
{
   private final NamedSupplier<java.lang.Package> packageSupplier;

   public PackageImpl(NamedSupplier<java.lang.Package> packageSupplier)
   {
      this.packageSupplier = packageSupplier;
   }

   @Override
   public R.Module getModule()
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
   public List<R.AnnotationUsage> getAnnotationUsages()
   {
      return Arrays.stream(packageSupplier.getInstance().getAnnotations())
                   .map(Adapter::generalize)
                   .toList();
   }

   @Override
   public List<R.AnnotationUsage> getDirectAnnotationUsages()
   {
      return Arrays.stream(packageSupplier.getInstance().getDeclaredAnnotations())
                   .map(Adapter::generalize)
                   .toList();
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
      return packageSupplier.getName();
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
      if (!(other instanceof C.Package otherPackage))
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
   public Implementation getImplementation()
   {
      return IMPLEMENTATION;
   }
}

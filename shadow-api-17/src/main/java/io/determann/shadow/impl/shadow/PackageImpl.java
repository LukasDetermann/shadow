package io.determann.shadow.impl.shadow;

import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.shadow.Declared;
import io.determann.shadow.api.shadow.Package;

import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.NoType;
import java.util.List;
import java.util.Objects;

public class PackageImpl extends ShadowImpl<NoType> implements Package
{
   private final PackageElement packageElement;

   public PackageImpl(ShadowApi shadowApi, PackageElement packageElement)
   {
      super(shadowApi, (NoType) packageElement.asType());
      this.packageElement = packageElement;
   }

   public PackageImpl(ShadowApi shadowApi, NoType noTypeMirror)
   {
      super(shadowApi, noTypeMirror);
      this.packageElement = getApi().getJdkApiContext().processingEnv().getElementUtils().getPackageElement(noTypeMirror.toString());
      if (packageElement == null)
      {
         throw new IllegalStateException(noTypeMirror + " is not unique");
      }
   }

   @Override
   public List<Declared> getContent()
   {
      return getElement().getEnclosedElements()
                         .stream()
                         .map(TypeElement.class::cast)
                         .map(typeElement -> getApi().getShadowFactory().<Declared>shadowFromElement(typeElement))
                         .toList();
   }

   @Override
   public boolean isUnnamed()
   {
      return getElement().isUnnamed();
   }

   @Override
   public PackageElement getElement()
   {
      return packageElement;
   }

   @Override
   public TypeKind getTypeKind()
   {
      return TypeKind.PACKAGE;
   }

   @Override
   public List<Declared> getDeclared()
   {
      return getContent();
   }

   @Override
   public Declared getDeclaredOrThrow(String qualifiedName)
   {
      TypeElement typeElement = getApi().getJdkApiContext().processingEnv().getElementUtils().getTypeElement(getModule().getElement(), qualifiedName);
      if (typeElement == null)
      {
         throw new IllegalArgumentException("no Declared found for \"" + qualifiedName + "\"");
      }
      return getApi().getShadowFactory().shadowFromElement(typeElement);
   }

   @Override
   public String toString()
   {
      return getElement().toString();
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getQualifiedName(),
                          getModule());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (other == null || !getClass().equals(other.getClass()))
      {
         return false;
      }
      PackageImpl otherPackage = (PackageImpl) other;
      return Objects.equals(getQualifiedName(), otherPackage.getQualifiedName()) &&
             Objects.equals(getModule(), otherPackage.getModule());
   }
}

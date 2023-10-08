package io.determann.shadow.impl.shadow;

import io.determann.shadow.api.MirrorAdapter;
import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.modifier.Modifier;
import io.determann.shadow.api.shadow.Declared;
import io.determann.shadow.api.shadow.Package;

import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.NoType;
import java.util.*;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

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
      this.packageElement = getApi().getJdkApiContext().getProcessingEnv().getElementUtils().getPackageElement(noTypeMirror.toString());
      if (packageElement == null)
      {
         throw new IllegalStateException(noTypeMirror + " is not unique");
      }
   }

   @Override
   public Set<Modifier> getModifiers()
   {
      return MirrorAdapter.getModifiers(getElement());
   }

   @Override
   public String getQualifiedName()
   {
      return getElement().getQualifiedName().toString();
   }

   @Override
   public List<Declared> getContent()
   {
      return getElement().getEnclosedElements()
                         .stream()
                         .map(TypeElement.class::cast)
                         .map(typeElement -> getApi().getShadowFactory().<Declared>shadowFromElement(typeElement))
                         .collect(collectingAndThen(toList(), Collections::unmodifiableList));
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
   public Optional<Declared> getDeclared(String qualifiedName)
   {
      return ofNullable(getApi().getJdkApiContext().getProcessingEnv().getElementUtils().getTypeElement(qualifiedName))
            .map(typeElement -> getApi().getShadowFactory().shadowFromElement(typeElement));
   }

   @Override
   public String toString()
   {
      return getElement().toString();
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
      if (other == null || !getClass().equals(other.getClass()))
      {
         return false;
      }
      PackageImpl otherPackage = (PackageImpl) other;
      return Objects.equals(getQualifiedName(), otherPackage.getQualifiedName());
   }
}

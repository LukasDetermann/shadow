package io.determann.shadow.impl.annotation_processing.shadow;

import io.determann.shadow.api.MirrorAdapter;
import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.annotation_processing.AnnotationProcessingContext;
import io.determann.shadow.api.modifier.Modifier;
import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.Declared;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;

import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.NoType;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toUnmodifiableList;

public class PackageImpl extends ShadowImpl<NoType> implements Package
{
   private final PackageElement packageElement;

   public PackageImpl(AnnotationProcessingContext annotationProcessingContext, PackageElement packageElement)
   {
      super(annotationProcessingContext, (NoType) packageElement.asType());
      this.packageElement = packageElement;
   }

   public PackageImpl(AnnotationProcessingContext annotationProcessingContext, NoType noTypeMirror)
   {
      super(annotationProcessingContext, noTypeMirror);
      this.packageElement = MirrorAdapter.getProcessingEnv(getApi()).getElementUtils().getPackageElement(noTypeMirror.toString());
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
                         .map(typeElement -> MirrorAdapter.<Declared>getShadow(getApi(), typeElement))
                         .collect(toUnmodifiableList());
   }

   @Override
   public boolean isUnnamed()
   {
      return getElement().isUnnamed();
   }

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
   public Optional<Declared> getDeclared(String qualifiedName)
   {
      return ofNullable(MirrorAdapter.getProcessingEnv(getApi()).getElementUtils().getTypeElement(qualifiedName))
            .map(typeElement -> MirrorAdapter.getShadow(getApi(), typeElement));
   }

   @Override
   public Module getModule()
   {
      return MirrorAdapter.getModule(getApi(), getElement());
   }

   @Override
   public String getSimpleName()
   {
      return MirrorAdapter.getSimpleName(getElement());
   }

   @Override
   public String getJavaDoc()
   {
      return MirrorAdapter.getJavaDoc(getApi(), getElement());
   }

   @Override
   public List<AnnotationUsage> getAnnotationUsages()
   {
      return MirrorAdapter.getAnnotationUsages(getApi(), getElement());
   }

   @Override
   public List<AnnotationUsage> getDirectAnnotationUsages()
   {
      return MirrorAdapter.getDirectAnnotationUsages(getApi(), getElement());
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

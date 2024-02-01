package io.determann.shadow.impl.lang_model.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.lang_model.MirrorAdapter;
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

public class PackageImpl extends ShadowImpl<NoType> implements Package
{
   private final PackageElement packageElement;

   public PackageImpl(LangModelContext context, PackageElement packageElement)
   {
      super(context, (NoType) packageElement.asType());
      this.packageElement = packageElement;
   }

   public PackageImpl(LangModelContext context, NoType noTypeMirror)
   {
      super(context, noTypeMirror);
      this.packageElement = MirrorAdapter.getElements(getApi()).getPackageElement(noTypeMirror.toString());
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
                         .map(typeElement -> MirrorAdapter.<Declared>getShadow(getApi(), typeElement))
                         .toList();
   }

   @Override
   public String getQualifiedName()
   {
      return getElement().getQualifiedName().toString();
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
      return getContent().stream()
                         .filter(declared -> declared.getQualifiedName().equals(qualifiedName))
                         .findFirst();
   }

   @Override
   public Module getModule()
   {
      return MirrorAdapter.getModule(getApi(), getElement());
   }

   @Override
   public String getName()
   {
      return MirrorAdapter.getName(getElement());
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
      if (!(other instanceof Package otherPackage))
      {
         return false;
      }
      return Objects.equals(getQualifiedName(), otherPackage.getQualifiedName()) &&
             Objects.equals(getModule(), otherPackage.getModule());
   }
}

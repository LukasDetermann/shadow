package io.determann.shadow.internal.lang_model.shadow;

import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.lang_model.query.PackageLangModel;
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

import static io.determann.shadow.api.lang_model.LangModelAdapter.generalize;
import static io.determann.shadow.internal.lang_model.LangModelProvider.IMPLEMENTATION_NAME;
import static io.determann.shadow.meta_meta.Operations.MODULE_ENCLOSED_GET_MODULE;
import static io.determann.shadow.meta_meta.Operations.QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME;
import static io.determann.shadow.meta_meta.Provider.requestOrThrow;

public class PackageImpl  implements PackageLangModel
{
   private final PackageElement packageElement;
   private final LangModelContext context;
   private final NoType noType;

   public PackageImpl(LangModelContext context, PackageElement packageElement)
   {
      this.context = context;
      this.packageElement = packageElement;
      this.noType = (NoType) packageElement.asType();
   }

   public PackageImpl(LangModelContext context, NoType noTypeMirror)
   {
      this.context = context;
      this.noType = noTypeMirror;
      this.packageElement = LangModelAdapter.getElements(getApi()).getPackageElement(noTypeMirror.toString());
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
                         .map(typeElement -> LangModelAdapter.<Declared>generalize(getApi(), typeElement))
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
   public List<Declared> getDeclared()
   {
      return getContent();
   }

   @Override
   public Optional<Declared> getDeclared(String qualifiedName)
   {
      return getContent().stream()
                         .filter(declared -> requestOrThrow(declared, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME).equals(qualifiedName))
                         .findFirst();
   }

   @Override
   public Module getModule()
   {
      return generalize(getApi(), LangModelAdapter.getElements(getApi()).getModuleOf(getElement()));
   }

   @Override
   public String getName()
   {
      return getElement().getSimpleName().toString();
   }

   @Override
   public String getJavaDoc()
   {
      return LangModelAdapter.getElements(getApi()).getDocComment(getElement());
   }

   @Override
   public List<AnnotationUsage> getAnnotationUsages()
   {
      return generalize(getApi(), LangModelAdapter.getElements(getApi()).getAllAnnotationMirrors(getElement()));
   }

   @Override
   public List<AnnotationUsage> getDirectAnnotationUsages()
   {
      return generalize(getApi(), getElement().getAnnotationMirrors());
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
      return Objects.equals(getQualifiedName(), requestOrThrow(otherPackage, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME)) &&
             Objects.equals(getModule(), requestOrThrow(otherPackage, MODULE_ENCLOSED_GET_MODULE));
   }

   @Override
   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
   }

   private LangModelContext getApi()
   {
      return context;
   }

   public NoType getMirror()
   {
      return noType;
   }
}

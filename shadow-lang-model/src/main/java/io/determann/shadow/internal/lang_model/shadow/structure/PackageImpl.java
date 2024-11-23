package io.determann.shadow.internal.lang_model.shadow.structure;

import io.determann.shadow.api.Implementation;
import io.determann.shadow.api.lang_model.LM_Adapter;
import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.shadow.LM_AnnotationUsage;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Module;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Package;
import io.determann.shadow.api.lang_model.shadow.type.LM_Declared;
import io.determann.shadow.api.shadow.structure.C_Package;

import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.NoType;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static io.determann.shadow.api.Operations.MODULE_ENCLOSED_GET_MODULE;
import static io.determann.shadow.api.Operations.QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.lang_model.LM_Adapter.generalize;

public class PackageImpl implements LM_Package
{
   private final PackageElement packageElement;
   private final LM_Context context;
   private final NoType noType;

   public PackageImpl(LM_Context context, PackageElement packageElement)
   {
      this.context = context;
      this.packageElement = packageElement;
      this.noType = (NoType) packageElement.asType();
   }

   public PackageImpl(LM_Context context, NoType noTypeMirror)
   {
      this.context = context;
      this.noType = noTypeMirror;
      this.packageElement = LM_Adapter.getElements(getApi()).getPackageElement(noTypeMirror.toString());
      if (packageElement == null)
      {
         throw new IllegalStateException(noTypeMirror + " is not unique");
      }
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
   public List<LM_Declared> getDeclared()
   {
      return getElement().getEnclosedElements()
                         .stream()
                         .map(TypeElement.class::cast)
                         .map(typeElement -> LM_Adapter.<LM_Declared>generalize(getApi(), typeElement))
                         .toList();
   }

   @Override
   public Optional<LM_Declared> getDeclared(String qualifiedName)
   {
      return getDeclared().stream()
                          .filter(declared -> requestOrThrow(declared, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME).equals(qualifiedName))
                          .findFirst();
   }

   @Override
   public LM_Module getModule()
   {
      return generalize(getApi(), LM_Adapter.getElements(getApi()).getModuleOf(getElement()));
   }

   @Override
   public String getName()
   {
      return getElement().getSimpleName().toString();
   }

   @Override
   public String getJavaDoc()
   {
      return LM_Adapter.getElements(getApi()).getDocComment(getElement());
   }

   @Override
   public List<LM_AnnotationUsage> getAnnotationUsages()
   {
      return generalize(getApi(), LM_Adapter.getElements(getApi()).getAllAnnotationMirrors(getElement()));
   }

   @Override
   public List<LM_AnnotationUsage> getDirectAnnotationUsages()
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
      if (!(other instanceof C_Package otherPackage))
      {
         return false;
      }
      return Objects.equals(getQualifiedName(), requestOrThrow(otherPackage, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME)) &&
             Objects.equals(getModule(), requestOrThrow(otherPackage, MODULE_ENCLOSED_GET_MODULE));
   }

   @Override
   public Implementation getImplementation()
   {
      return getApi().getImplementation();
   }

   private LM_Context getApi()
   {
      return context;
   }

   public NoType getMirror()
   {
      return noType;
   }
}

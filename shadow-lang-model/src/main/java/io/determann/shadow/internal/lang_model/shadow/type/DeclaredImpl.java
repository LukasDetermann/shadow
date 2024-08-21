package io.determann.shadow.internal.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.lang_model.shadow.*;
import io.determann.shadow.api.lang_model.shadow.structure.*;
import io.determann.shadow.api.lang_model.shadow.type.DeclaredLangModel;
import io.determann.shadow.api.lang_model.shadow.type.InterfaceLangModel;
import io.determann.shadow.api.shadow.NestingKind;
import io.determann.shadow.api.shadow.TypeKind;
import io.determann.shadow.api.shadow.modifier.Modifier;
import io.determann.shadow.api.shadow.type.Declared;
import io.determann.shadow.api.shadow.type.Shadow;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.ElementFilter;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.lang_model.LangModelAdapter.*;

public class DeclaredImpl extends ShadowImpl<DeclaredType> implements DeclaredLangModel,
                                                                      NameableLangModel,
                                                                      QualifiedNameableLamgModel,
                                                                      ModuleEnclosedLangModel,
                                                                      DocumentedLangModel
{
   private final TypeElement typeElement;

   DeclaredImpl(LangModelContext context, DeclaredType declaredTypeMirror)
   {
      super(context, declaredTypeMirror);
      this.typeElement = (TypeElement) declaredTypeMirror.asElement();
   }

   DeclaredImpl(LangModelContext context, TypeElement typeElement)
   {
      super(context, (DeclaredType) typeElement.asType());
      this.typeElement = typeElement;
   }

   @Override
   public Set<Modifier> getModifiers()
   {
      return LangModelAdapter.getModifiers(getElement());
   }

   @Override
   public boolean isSubtypeOf(Shadow shadow)
   {
      return LangModelAdapter.getTypes(getApi()).isSubtype(getMirror(), LangModelAdapter.particularType((DeclaredLangModel) shadow));
   }

   @Override
   public TypeKind getKind()
   {
      return switch (getElement().getKind())
      {
         case ENUM -> TypeKind.ENUM;
         case CLASS -> TypeKind.CLASS;
         case INTERFACE -> TypeKind.INTERFACE;
         case ANNOTATION_TYPE -> TypeKind.ANNOTATION;
         case RECORD -> TypeKind.RECORD;
         default -> throw new IllegalStateException();
      };
   }

   public TypeElement getElement()
   {
      return typeElement;
   }

   @Override
   public NestingKind getNesting()
   {
      return switch (getElement().getNestingKind())
      {
         case MEMBER -> NestingKind.INNER;
         case TOP_LEVEL -> NestingKind.OUTER;
         default -> throw new IllegalStateException();
      };
   }

   @Override
   public List<FieldLangModel> getFields()
   {
      return getElement().getEnclosedElements()
                         .stream()
                         .filter(element -> element.getKind().equals(ElementKind.FIELD))
                         .map(variableElement -> LangModelAdapter.<FieldLangModel>generalize(getApi(), variableElement))
                         .toList();
   }

   @Override
   public List<MethodLangModel> getMethods()
   {
      return ElementFilter.methodsIn(getElement().getEnclosedElements())
                          .stream()
                          .map(element -> LangModelAdapter.generalize(getApi(), element))
                          .map(MethodLangModel.class::cast)
                          .toList();
   }

   @Override
   public List<ConstructorLangModel> getConstructors()
   {
      return ElementFilter.constructorsIn(getElement().getEnclosedElements())
                          .stream()
                          .map(element -> LangModelAdapter.generalize(getApi(), element))
                          .map(ConstructorLangModel.class::cast)
                          .toList();
   }

   @Override
   public List<DeclaredLangModel> getDirectSuperTypes()
   {
      return LangModelAdapter.getTypes(getApi())
                             .directSupertypes(getMirror())
                             .stream()
                             .map(typeMirror1 -> LangModelAdapter.<DeclaredLangModel>generalize(getApi(), typeMirror1))
                             .toList();
   }

   @Override
   public Set<DeclaredLangModel> getSuperTypes()
   {
      return findAllSupertypes(new HashSet<>(), this);
   }

   private Set<DeclaredLangModel> findAllSupertypes(Set<DeclaredLangModel> found, Declared declared)
   {
      List<DeclaredLangModel> directSupertypes = (List<DeclaredLangModel>) requestOrThrow(declared, DECLARED_GET_SUPER_TYPES);
      found.addAll(directSupertypes);
      for (Declared directSupertype : directSupertypes)
      {
         findAllSupertypes(found, directSupertype);
      }
      return found;
   }

   @Override
   public String getQualifiedName()
   {
      return getElement().getQualifiedName().toString();
   }

   @Override
   public String getBinaryName()
   {
      return getElements(getApi()).getBinaryName(getElement()).toString();
   }

   @Override
   public List<InterfaceLangModel> getDirectInterfaces()
   {
      return getElement().getInterfaces()
                         .stream()
                         .map(typeMirror -> LangModelAdapter.<InterfaceLangModel>generalize(getApi(), typeMirror))
                         .toList();
   }

   @Override
   public List<InterfaceLangModel> getInterfaces()
   {
      return getSuperTypes().stream()
                            .filter(declared -> TypeKind.INTERFACE.equals(requestOrThrow(declared, SHADOW_GET_KIND)))
                            .map(InterfaceLangModel.class::cast)
                            .toList();
   }

   @Override
   public PackageLangModel getPackage()
   {
      return generalizePackage(getApi(), getElements(getApi()).getPackageOf(getElement()));
   }

   @Override
   public ModuleLangModel getModule()
   {
      return generalize(getApi(), getElements(getApi()).getModuleOf(getElement()));
   }

   @Override
   public String getName()
   {
      return getElement().getSimpleName().toString();
   }

   @Override
   public String getJavaDoc()
   {
      return getElements(getApi()).getDocComment(getElement());
   }

   @Override
   public List<AnnotationUsageLangModel> getAnnotationUsages()
   {
      return generalize(getApi(), getElements(getApi()).getAllAnnotationMirrors(getElement()));
   }

   @Override
   public List<AnnotationUsageLangModel> getDirectAnnotationUsages()
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
      return Objects.hash(getKind(),
                          getQualifiedName(),
                          getModifiers());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof Declared otherDeclared))
      {
         return false;
      }
      return Objects.equals(getQualifiedName(), requestOrThrow(otherDeclared, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME)) &&
             Objects.equals(getKind(), requestOrThrow(otherDeclared, SHADOW_GET_KIND)) &&
             requestOrEmpty(otherDeclared, MODIFIABLE_GET_MODIFIERS).map(modifiers -> Objects.equals(modifiers, getModifiers())).orElse(false);
   }
}

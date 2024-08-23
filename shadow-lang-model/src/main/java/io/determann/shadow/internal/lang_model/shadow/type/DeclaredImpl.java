package io.determann.shadow.internal.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.LM_Adapter;
import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.shadow.*;
import io.determann.shadow.api.lang_model.shadow.structure.*;
import io.determann.shadow.api.lang_model.shadow.type.LM_Declared;
import io.determann.shadow.api.lang_model.shadow.type.LM_Interface;
import io.determann.shadow.api.shadow.C_NestingKind;
import io.determann.shadow.api.shadow.C_TypeKind;
import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.type.C_Declared;
import io.determann.shadow.api.shadow.type.C_Shadow;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.ElementFilter;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.lang_model.LM_Adapter.*;

public class DeclaredImpl extends ShadowImpl<DeclaredType> implements LM_Declared,
                                                                      LM_Nameable,
                                                                      LM_QualifiedNameable,
                                                                      LM_ModuleEnclosed,
                                                                      LM_Documented
{
   private final TypeElement typeElement;

   DeclaredImpl(LM_Context context, DeclaredType declaredTypeMirror)
   {
      super(context, declaredTypeMirror);
      this.typeElement = (TypeElement) declaredTypeMirror.asElement();
   }

   DeclaredImpl(LM_Context context, TypeElement typeElement)
   {
      super(context, (DeclaredType) typeElement.asType());
      this.typeElement = typeElement;
   }

   @Override
   public Set<C_Modifier> getModifiers()
   {
      return LM_Adapter.getModifiers(getElement());
   }

   @Override
   public boolean isSubtypeOf(C_Shadow shadow)
   {
      return LM_Adapter.getTypes(getApi()).isSubtype(getMirror(), LM_Adapter.particularType((LM_Declared) shadow));
   }

   @Override
   public C_TypeKind getKind()
   {
      return switch (getElement().getKind())
      {
         case ENUM -> C_TypeKind.ENUM;
         case CLASS -> C_TypeKind.CLASS;
         case INTERFACE -> C_TypeKind.INTERFACE;
         case ANNOTATION_TYPE -> C_TypeKind.ANNOTATION;
         case RECORD -> C_TypeKind.RECORD;
         default -> throw new IllegalStateException();
      };
   }

   public TypeElement getElement()
   {
      return typeElement;
   }

   @Override
   public C_NestingKind getNesting()
   {
      return switch (getElement().getNestingKind())
      {
         case MEMBER -> C_NestingKind.INNER;
         case TOP_LEVEL -> C_NestingKind.OUTER;
         default -> throw new IllegalStateException();
      };
   }

   @Override
   public List<LM_Field> getFields()
   {
      return getElement().getEnclosedElements()
                         .stream()
                         .filter(element -> element.getKind().equals(ElementKind.FIELD))
                         .map(VariableElement.class::cast)
                         .map(variableElement -> LM_Adapter.generalize(getApi(), variableElement))
                         .map(LM_Field.class::cast)
                         .toList();
   }

   @Override
   public List<LM_Method> getMethods()
   {
      return ElementFilter.methodsIn(getElement().getEnclosedElements())
                          .stream()
                          .map(element -> LM_Adapter.generalize(getApi(), element))
                          .map(LM_Method.class::cast)
                          .toList();
   }

   @Override
   public List<LM_Constructor> getConstructors()
   {
      return ElementFilter.constructorsIn(getElement().getEnclosedElements())
                          .stream()
                          .map(element -> LM_Adapter.generalize(getApi(), element))
                          .map(LM_Constructor.class::cast)
                          .toList();
   }

   @Override
   public List<LM_Declared> getDirectSuperTypes()
   {
      return LM_Adapter.getTypes(getApi())
                       .directSupertypes(getMirror())
                       .stream()
                       .map(typeMirror1 -> LM_Adapter.<LM_Declared>generalize(getApi(), typeMirror1))
                       .toList();
   }

   @Override
   public Set<LM_Declared> getSuperTypes()
   {
      return findAllSupertypes(new HashSet<>(), this);
   }

   private Set<LM_Declared> findAllSupertypes(Set<LM_Declared> found, C_Declared declared)
   {
      List<LM_Declared> directSupertypes = (List<LM_Declared>) requestOrThrow(declared, DECLARED_GET_SUPER_TYPES);
      found.addAll(directSupertypes);
      for (C_Declared directSupertype : directSupertypes)
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
   public List<LM_Interface> getDirectInterfaces()
   {
      return getElement().getInterfaces()
                         .stream()
                         .map(typeMirror -> LM_Adapter.<LM_Interface>generalize(getApi(), typeMirror))
                         .toList();
   }

   @Override
   public List<LM_Interface> getInterfaces()
   {
      return getSuperTypes().stream()
                            .filter(declared -> C_TypeKind.INTERFACE.equals(requestOrThrow(declared, SHADOW_GET_KIND)))
                            .map(LM_Interface.class::cast)
                            .toList();
   }

   @Override
   public LM_Package getPackage()
   {
      return generalizePackage(getApi(), getElements(getApi()).getPackageOf(getElement()));
   }

   @Override
   public LM_Module getModule()
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
   public List<LM_AnnotationUsage> getAnnotationUsages()
   {
      return generalize(getApi(), getElements(getApi()).getAllAnnotationMirrors(getElement()));
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
      if (!(other instanceof C_Declared otherDeclared))
      {
         return false;
      }
      return Objects.equals(getQualifiedName(), requestOrThrow(otherDeclared, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME)) &&
             Objects.equals(getKind(), requestOrThrow(otherDeclared, SHADOW_GET_KIND)) &&
             requestOrEmpty(otherDeclared, MODIFIABLE_GET_MODIFIERS).map(modifiers -> Objects.equals(modifiers, getModifiers())).orElse(false);
   }
}

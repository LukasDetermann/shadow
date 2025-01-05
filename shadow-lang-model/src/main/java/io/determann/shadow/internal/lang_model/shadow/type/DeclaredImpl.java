package io.determann.shadow.internal.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.adapter.LM_Adapters;
import io.determann.shadow.api.lang_model.shadow.LM_AnnotationUsage;
import io.determann.shadow.api.lang_model.shadow.structure.*;
import io.determann.shadow.api.lang_model.shadow.type.LM_Array;
import io.determann.shadow.api.lang_model.shadow.type.LM_Declared;
import io.determann.shadow.api.lang_model.shadow.type.LM_Interface;
import io.determann.shadow.api.lang_model.shadow.type.LM_Wildcard;
import io.determann.shadow.api.shadow.C_NestingKind;
import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.type.C_Declared;
import io.determann.shadow.api.shadow.type.C_Type;
import io.determann.shadow.internal.lang_model.LangModelContextImpl;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.ElementFilter;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static io.determann.shadow.api.Operations.MODIFIABLE_GET_MODIFIERS;
import static io.determann.shadow.api.Operations.QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.lang_model.adapter.LM_Adapters.adapt;

public class DeclaredImpl extends TypeImpl<DeclaredType> implements LM_Declared
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
      return LangModelContextImpl.getModifiers(getElement());
   }

   @Override
   public boolean isSubtypeOf(C_Type type)
   {
      return adapt(getApi()).toTypes().isSubtype(getMirror(), adapt((LM_Declared) type).toDeclaredType());
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
                         .map(variableElement -> adapt(getApi(), variableElement))
                         .map(LM_Field.class::cast)
                         .toList();
   }

   @Override
   public List<LM_Method> getMethods()
   {
      return ElementFilter.methodsIn(getElement().getEnclosedElements())
                          .stream()
                          .map(element -> adapt(getApi(), element))
                          .map(LM_Method.class::cast)
                          .toList();
   }

   public List<LM_Constructor> getConstructors()
   {
      return ElementFilter.constructorsIn(getElement().getEnclosedElements())
                          .stream()
                          .map(element -> adapt(getApi(), element))
                          .map(LM_Constructor.class::cast)
                          .toList();
   }

   @Override
   public List<LM_Declared> getDirectSuperTypes()
   {
      return adapt(getApi()).toTypes()
                        .directSupertypes(getMirror())
                        .stream()
                        .map(typeMirror1 -> adapt(getApi(), ((DeclaredType) typeMirror1)))
                        .toList();
   }

   @Override
   public Set<LM_Declared> getSuperTypes()
   {
      return findAllSupertypes(new HashSet<>(), this);
   }

   private Set<LM_Declared> findAllSupertypes(Set<LM_Declared> found, LM_Declared declared)
   {
      List<LM_Declared> directSupertypes = declared.getDirectSuperTypes();
      found.addAll(directSupertypes);
      for (LM_Declared directSupertype : directSupertypes)
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
      return adapt(getApi()).toElements().getBinaryName(getElement()).toString();
   }

   @Override
   public LM_Array asArray()
   {
      return adapt(getApi(), adapt(getApi()).toTypes().getArrayType(getMirror()));
   }

   @Override
   public LM_Wildcard asExtendsWildcard()
   {
      return adapt(getApi(), adapt(getApi()).toTypes().getWildcardType(getMirror(), null));
   }

   @Override
   public LM_Wildcard asSuperWildcard()
   {
      return adapt(getApi(), adapt(getApi()).toTypes().getWildcardType(null, getMirror()));
   }

   @Override
   public List<LM_Interface> getDirectInterfaces()
   {
      return getElement().getInterfaces()
                         .stream()
                         .map(typeMirror -> LM_Adapters.adapt(getApi(), ((DeclaredType) typeMirror)))
                         .map(LM_Interface.class::cast)
                         .toList();
   }

   @Override
   public List<LM_Interface> getInterfaces()
   {
      return getSuperTypes().stream()
                            .filter(declared -> declared instanceof LM_Interface)
                            .map(LM_Interface.class::cast)
                            .toList();
   }

   @Override
   public LM_Package getPackage()
   {
      return adapt(getApi(), adapt(getApi()).toElements().getPackageOf(getElement()));
   }

   @Override
   public LM_Module getModule()
   {
      return adapt(getApi(), adapt(getApi()).toElements().getModuleOf(getElement()));
   }

   @Override
   public String getName()
   {
      return getElement().getSimpleName().toString();
   }

   @Override
   public String getJavaDoc()
   {
      return adapt(getApi()).toElements().getDocComment(getElement());
   }

   @Override
   public List<LM_AnnotationUsage> getAnnotationUsages()
   {
      return adapt(getApi(), adapt(getApi()).toElements().getAllAnnotationMirrors(getElement()));
   }

   @Override
   public List<LM_AnnotationUsage> getDirectAnnotationUsages()
   {
      return adapt(getApi(), getElement().getAnnotationMirrors());
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
             requestOrEmpty(otherDeclared, MODIFIABLE_GET_MODIFIERS).map(modifiers -> Objects.equals(modifiers, getModifiers())).orElse(false);
   }
}

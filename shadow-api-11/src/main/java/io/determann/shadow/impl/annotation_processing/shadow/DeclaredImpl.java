package io.determann.shadow.impl.annotation_processing.shadow;

import io.determann.shadow.api.MirrorAdapter;
import io.determann.shadow.api.NestingKind;
import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.converter.DeclaredConverter;
import io.determann.shadow.api.modifier.Modifier;
import io.determann.shadow.api.shadow.Enum;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.*;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.ElementFilter;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toUnmodifiableList;

public class DeclaredImpl extends ShadowImpl<DeclaredType> implements Annotation,
                                                                      Enum
{
   private final TypeElement typeElement;

   public DeclaredImpl(ShadowApi shadowApi, DeclaredType declaredTypeMirror)
   {
      super(shadowApi, declaredTypeMirror);
      this.typeElement = (TypeElement) declaredTypeMirror.asElement();
   }

   public DeclaredImpl(ShadowApi shadowApi, TypeElement typeElement)
   {
      super(shadowApi, (DeclaredType) typeElement.asType());
      this.typeElement = typeElement;
   }

   @Override
   public Set<Modifier> getModifiers()
   {
      return MirrorAdapter.getModifiers(getElement());
   }

   @Override
   public boolean isSubtypeOf(Shadow shadow)
   {
      return MirrorAdapter.getProcessingEnv(getApi()).getTypeUtils().isSubtype(getMirror(), MirrorAdapter.getType(shadow));
   }

   @Override
   public TypeKind getTypeKind()
   {
      switch (getElement().getKind())
      {
         case ENUM:
            return TypeKind.ENUM;
         case CLASS:
            return TypeKind.CLASS;
         case INTERFACE:
            return TypeKind.INTERFACE;
         case ANNOTATION_TYPE:
            return TypeKind.ANNOTATION;
         default:
            throw new IllegalStateException();
      }
   }

   public TypeElement getElement()
   {
      return typeElement;
   }

   @Override
   public NestingKind getNesting()
   {
      switch (getElement().getNestingKind())
      {
         case MEMBER:
            return NestingKind.INNER;
         case TOP_LEVEL:
            return NestingKind.OUTER;
         default:
            throw new IllegalStateException();
      }
   }

   @Override
   public String getQualifiedName()
   {
      return getElement().getQualifiedName().toString();
   }


   @Override
   public Field getFieldOrThrow(String simpleName)
   {
      return getFields().stream().filter(field -> field.getSimpleName().equals(simpleName)).findAny().orElseThrow();
   }

   @Override
   public List<Field> getFields()
   {
      return getElement().getEnclosedElements()
                         .stream()
                         .filter(element -> element.getKind().equals(ElementKind.FIELD))
                         .map(variableElement -> MirrorAdapter.<Field>getShadow(getApi(), variableElement))
                         .collect(toUnmodifiableList());
   }

   @Override
   public List<Method> getMethods(String simpleName)
   {
      return getMethods().stream().filter(field -> field.getSimpleName().equals(simpleName)).collect(toUnmodifiableList());
   }

   @Override
   public List<Method> getMethods()
   {
      return ElementFilter.methodsIn(getElement().getEnclosedElements())
                          .stream()
                          .map(element -> MirrorAdapter.<Method>getShadow(getApi(), element))
                          .collect(toUnmodifiableList());
   }

   @Override
   public List<Constructor> getConstructors()
   {
      return ElementFilter.constructorsIn(getElement().getEnclosedElements())
                          .stream()
                          .map(element -> MirrorAdapter.<Constructor>getShadow(getApi(), element))
                          .collect(toUnmodifiableList());
   }

   @Override
   public List<Declared> getDirectSuperTypes()
   {
      return MirrorAdapter.getProcessingEnv(getApi()).getTypeUtils()
                          .directSupertypes(getMirror())
                          .stream()
                          .map(typeMirror1 -> MirrorAdapter.<Declared>getShadow(getApi(), typeMirror1))
                          .collect(toUnmodifiableList());
   }

   @Override
   public Set<Declared> getSuperTypes()
   {
      return findAllSupertypes(new HashSet<>(), this);
   }

   private Set<Declared> findAllSupertypes(Set<Declared> found, Declared declared)
   {
      List<Declared> directSupertypes = declared.getDirectSuperTypes();
      found.addAll(directSupertypes);
      for (Declared directSupertype : directSupertypes)
      {
         findAllSupertypes(found, directSupertype);
      }
      return found;
   }

   @Override
   public String getBinaryName()
   {
      return MirrorAdapter.getProcessingEnv(getApi()).getElementUtils().getBinaryName(getElement()).toString();
   }

   @Override
   public Wildcard asExtendsWildcard()
   {
      return MirrorAdapter
                     .getShadow(getApi(), MirrorAdapter.getProcessingEnv(getApi()).getTypeUtils().getWildcardType(getMirror(), null));
   }

   @Override
   public Wildcard asSuperWildcard()
   {
      return MirrorAdapter
                     .getShadow(getApi(), MirrorAdapter.getProcessingEnv(getApi()).getTypeUtils().getWildcardType(null, getMirror()));
   }

   @Override
   public Array asArray()
   {
      return MirrorAdapter.getShadow(getApi(), MirrorAdapter.getProcessingEnv(getApi()).getTypeUtils().getArrayType(getMirror()));
   }

   @Override
   public List<Interface> getDirectInterfaces()
   {
      return getElement().getInterfaces()
                         .stream()
                         .map(typeMirror -> MirrorAdapter.<Interface>getShadow(getApi(), typeMirror))
                         .collect(toUnmodifiableList());
   }

   @Override
   public List<Interface> getInterfaces()
   {
      return getSuperTypes().stream()
                            .filter(declared -> declared.getTypeKind().equals(TypeKind.INTERFACE))
                            .map(ShadowApi::convert)
                            .map(DeclaredConverter::toInterfaceOrThrow)
                            .collect(toUnmodifiableList());
   }

   @Override
   public Interface getInterfaceOrThrow(String qualifiedName)
   {
      return getInterfaces().stream()
                            .filter(anInterface -> anInterface.getQualifiedName().equals(qualifiedName))
                            .findAny()
                            .orElseThrow();
   }

   @Override
   public Interface getDirectInterfaceOrThrow(String qualifiedName)
   {
      return getDirectInterfaces().stream()
                                  .filter(anInterface -> anInterface.getQualifiedName().equals(qualifiedName))
                                  .findAny()
                                  .orElseThrow();
   }

   @Override
   public List<EnumConstant> getEumConstants()
   {
      return getElement().getEnclosedElements()
                         .stream()
                         .filter(element -> element.getKind().equals(ElementKind.ENUM_CONSTANT))
                         .map(VariableElement.class::cast)
                         .map(variableElement -> MirrorAdapter.<EnumConstant>getShadow(getApi(), variableElement))
                         .collect(toUnmodifiableList());
   }

   @Override
   public EnumConstant getEnumConstantOrThrow(String simpleName)
   {
      return getEumConstants().stream().filter(field -> field.getSimpleName().equals(simpleName)).findAny().orElseThrow();
   }

   @Override
   public Package getPackage()
   {
      return MirrorAdapter
                     .getShadow(getApi(), MirrorAdapter.getProcessingEnv(getApi()).getElementUtils().getPackageOf(getElement()));
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
   public void logError(String msg)
   {
      MirrorAdapter.logError(getApi(), getElement(), msg);
   }

   @Override
   public void logInfo(String msg)
   {
      MirrorAdapter.logInfo(getApi(), getElement(), msg);
   }

   @Override
   public void logWarning(String msg)
   {
      MirrorAdapter.logWarning(getApi(), getElement(), msg);
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
      return Objects.hash(getTypeKind(),
                          getQualifiedName(),
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
      DeclaredImpl otherDeclared = (DeclaredImpl) other;
      return Objects.equals(getQualifiedName(), otherDeclared.getQualifiedName()) &&
             Objects.equals(getTypeKind(), otherDeclared.getTypeKind()) &&
             Objects.equals(getModule(), otherDeclared.getModule());
   }
}

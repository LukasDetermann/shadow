package io.determann.shadow.impl.shadow;

import io.determann.shadow.api.MirrorAdapter;
import io.determann.shadow.api.NestingKind;
import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.converter.DeclaredConverter;
import io.determann.shadow.api.shadow.Enum;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.*;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.ElementFilter;
import java.util.*;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

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
   public boolean isSubtypeOf(Shadow shadow)
   {
      return getApi().getJdkApiContext().getProcessingEnv().getTypeUtils().isSubtype(getMirror(), MirrorAdapter.getType(shadow));
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

   @Override
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
   public Field getFieldOrThrow(String simpleName)
   {
      return getFields().stream().filter(field -> field.getSimpleName().equals(simpleName)).findAny().orElseThrow(NoSuchElementException::new);
   }

   @Override
   public List<Field> getFields()
   {
      return getElement().getEnclosedElements()
                         .stream()
                         .filter(element -> element.getKind().equals(ElementKind.FIELD))
                         .map(variableElement -> getApi().getShadowFactory().<Field>shadowFromElement(variableElement))
                         .collect(collectingAndThen(toList(), Collections::unmodifiableList));
   }

   @Override
   public List<Method> getMethods(String simpleName)
   {
      return getMethods().stream()
                         .filter(field -> field.getSimpleName().equals(simpleName))
                         .collect(collectingAndThen(toList(), Collections::unmodifiableList));
   }

   @Override
   public List<Method> getMethods()
   {
      return ElementFilter.methodsIn(getElement().getEnclosedElements())
                          .stream()
                          .map(element -> getApi().getShadowFactory().<Method>shadowFromElement(element))
                          .collect(collectingAndThen(toList(), Collections::unmodifiableList));
   }

   @Override
   public List<Constructor> getConstructors()
   {
      return ElementFilter.constructorsIn(getElement().getEnclosedElements())
                          .stream()
                          .map(element -> getApi().getShadowFactory().<Constructor>shadowFromElement(element))
                          .collect(collectingAndThen(toList(), Collections::unmodifiableList));
   }

   @Override
   public List<Declared> getDirectSuperTypes()
   {
      return getApi().getJdkApiContext().getProcessingEnv().getTypeUtils()
                     .directSupertypes(getMirror())
                     .stream()
                     .map(typeMirror1 -> getApi().getShadowFactory().<Declared>shadowFromType(typeMirror1))
                     .collect(collectingAndThen(toList(), Collections::unmodifiableList));
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
      return getApi().getJdkApiContext().getProcessingEnv().getElementUtils().getBinaryName(getElement()).toString();
   }

   @Override
   public Wildcard asExtendsWildcard()
   {
      return getApi().getShadowFactory()
                     .shadowFromType(getApi().getJdkApiContext().getProcessingEnv().getTypeUtils().getWildcardType(getMirror(), null));
   }

   @Override
   public Wildcard asSuperWildcard()
   {
      return getApi().getShadowFactory()
                     .shadowFromType(getApi().getJdkApiContext().getProcessingEnv().getTypeUtils().getWildcardType(null, getMirror()));
   }

   @Override
   public Array asArray()
   {
      return getApi().getShadowFactory().shadowFromType(getApi().getJdkApiContext().getProcessingEnv().getTypeUtils().getArrayType(getMirror()));
   }

   @Override
   public List<Interface> getDirectInterfaces()
   {
      return getElement().getInterfaces()
                         .stream()
                         .map(typeMirror -> getApi().getShadowFactory().<Interface>shadowFromType(typeMirror))
                         .collect(collectingAndThen(toList(), Collections::unmodifiableList));
   }

   @Override
   public List<Interface> getInterfaces()
   {
      return getSuperTypes().stream()
                            .filter(declared -> declared.getTypeKind().equals(TypeKind.INTERFACE))
                            .map(ShadowApi::convert)
                            .map(DeclaredConverter::toInterfaceOrThrow)
                            .collect(collectingAndThen(toList(), Collections::unmodifiableList));
   }

   @Override
   public Interface getInterfaceOrThrow(String qualifiedName)
   {
      return getInterfaces().stream()
                            .filter(anInterface -> anInterface.getQualifiedName().equals(qualifiedName))
                            .findAny()
                            .orElseThrow(NoSuchElementException::new);
   }

   @Override
   public Interface getDirectInterfaceOrThrow(String qualifiedName)
   {
      return getDirectInterfaces().stream()
                                  .filter(anInterface -> anInterface.getQualifiedName().equals(qualifiedName))
                                  .findAny()
                                  .orElseThrow(NoSuchElementException::new);
   }

   @Override
   public List<EnumConstant> getEumConstants()
   {
      return getElement().getEnclosedElements()
                         .stream()
                         .filter(element -> element.getKind().equals(ElementKind.ENUM_CONSTANT))
                         .map(VariableElement.class::cast)
                         .map(variableElement -> getApi().getShadowFactory().<EnumConstant>shadowFromElement(variableElement))
                         .collect(collectingAndThen(toList(), Collections::unmodifiableList));
   }

   @Override
   public EnumConstant getEnumConstantOrThrow(String simpleName)
   {
      return getEumConstants().stream()
                              .filter(field -> field.getSimpleName().equals(simpleName))
                              .findAny()
                              .orElseThrow(NoSuchElementException::new);
   }

   @Override
   public Package getPackage()
   {
      return getApi().getShadowFactory()
                     .shadowFromElement(getApi().getJdkApiContext().getProcessingEnv().getElementUtils().getPackageOf(getElement()));
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
                          getQualifiedName());
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
             Objects.equals(getTypeKind(), otherDeclared.getTypeKind());
   }
}

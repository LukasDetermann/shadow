package org.determann.shadow.impl.shadow;

import org.determann.shadow.api.NestingKind;
import org.determann.shadow.api.ShadowApi;
import org.determann.shadow.api.TypeKind;
import org.determann.shadow.api.converter.DeclaredConverter;
import org.determann.shadow.api.shadow.Enum;
import org.determann.shadow.api.shadow.Package;
import org.determann.shadow.api.shadow.Record;
import org.determann.shadow.api.shadow.*;
import org.jetbrains.annotations.UnmodifiableView;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class DeclaredImpl extends ShadowImpl<DeclaredType> implements Annotation,
                                                                      Enum,
                                                                      Record
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
   public boolean isSubtypeOf(Shadow<? extends TypeMirror> shadow)
   {
      return getApi().getJdkApiContext().types().isSubtype(getMirror(), shadow.getMirror());
   }

   @Override
   public TypeKind getTypeKind()
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

   @Override
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
                         .map(variableElement -> getApi().getShadowFactory().<Field>shadowFromElement(variableElement))
                         .toList();
   }

   @Override
   public List<Method> getMethods(String simpleName)
   {
      return getMethods().stream().filter(field -> field.getSimpleName().equals(simpleName)).toList();
   }

   @Override
   public List<Method> getMethods()
   {
      return ElementFilter.methodsIn(getElement().getEnclosedElements())
                          .stream()
                          .map(element -> getApi().getShadowFactory().<Method>shadowFromElement(element))
                          .toList();
   }

   @Override
   public List<Constructor> getConstructors()
   {
      return ElementFilter.constructorsIn(getElement().getEnclosedElements())
                          .stream()
                          .map(element -> getApi().getShadowFactory().<Constructor>shadowFromElement(element))
                          .toList();
   }

   @Override
   public List<Declared> getDirectSuperTypes()
   {
      return getApi().getJdkApiContext().types()
                     .directSupertypes(getMirror())
                     .stream()
                     .map(typeMirror1 -> getApi().getShadowFactory().<Declared>shadowFromType(typeMirror1))
                     .toList();
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
      return getApi().getJdkApiContext().elements().getBinaryName(getElement()).toString();
   }

   @Override
   public Wildcard asExtendsWildcard()
   {
      return getApi().getShadowFactory().shadowFromType(getApi().getJdkApiContext().types().getWildcardType(getMirror(), null));
   }

   @Override
   public Wildcard asSuperWildcard()
   {
      return getApi().getShadowFactory().shadowFromType(getApi().getJdkApiContext().types().getWildcardType(null, getMirror()));
   }

   @Override
   public List<Interface> getDirectInterfaces()
   {
      return getElement().getInterfaces()
                         .stream()
                         .map(typeMirror -> getApi().getShadowFactory().<Interface>shadowFromType(typeMirror))
                         .toList();
   }

   @Override
   public @UnmodifiableView List<Interface> getInterfaces()
   {
      return getSuperTypes().stream()
                            .filter(declared -> declared.getTypeKind().equals(TypeKind.INTERFACE))
                            .map(ShadowApi::convert)
                            .map(DeclaredConverter::toInterfaceOrThrow)
                            .toList();
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
                         .map(variableElement -> getApi().getShadowFactory().<EnumConstant>shadowFromElement(variableElement))
                         .toList();
   }

   @Override
   public EnumConstant getEnumConstantOrThrow(String simpleName)
   {
      return getEumConstants().stream().filter(field -> field.getSimpleName().equals(simpleName)).findAny().orElseThrow();
   }

   @Override
   public Package getPackage()
   {
      return getApi().getShadowFactory().shadowFromElement(getApi().getJdkApiContext().elements().getPackageOf(getElement()));
   }

   @Override
   public RecordComponent getRecordComponentOrThrow(String simpleName)
   {
      return getRecordComponents().stream().filter(field -> field.getSimpleName().equals(simpleName)).findAny().orElseThrow();
   }

   @Override
   public List<RecordComponent> getRecordComponents()
   {
      return getElement().getRecordComponents()
                         .stream()
                         .map(recordComponentElement -> getApi().getShadowFactory().<RecordComponent>shadowFromElement(recordComponentElement))
                         .toList();
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

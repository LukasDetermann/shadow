package io.determann.shadow.impl.reflection.shadow;

import io.determann.shadow.api.NestingKind;
import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.converter.Converter;
import io.determann.shadow.api.converter.DeclaredConverter;
import io.determann.shadow.api.converter.ShadowConverter;
import io.determann.shadow.api.modifier.Modifier;
import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.shadow.Enum;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.*;
import io.determann.shadow.impl.reflection.ReflectionUtil;

import java.lang.Class;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public class DeclaredImpl implements Annotation,
                                     Enum
{
   private final Class<?> aClass;

   public DeclaredImpl(Class<?> aClass)
   {
      this.aClass = aClass;
   }

   @Override
   public String getName()
   {
      return getaClass().getSimpleName();
   }

   @Override
   public String getJavaDoc()
   {
      return null;
   }

   @Override
   public List<AnnotationUsage> getAnnotationUsages()
   {
      return stream(getaClass().getAnnotations())
            .map(ReflectionAdapter::getAnnotationUsage)
            .collect(collectingAndThen(toList(), Collections::unmodifiableList));
   }

   @Override
   public List<AnnotationUsage> getDirectAnnotationUsages()
   {
      return stream(getaClass().getDeclaredAnnotations())
            .map(ReflectionAdapter::getAnnotationUsage)
            .collect(collectingAndThen(toList(), Collections::unmodifiableList));
   }

   @Override
   public String getQualifiedName()
   {
      return getaClass().getCanonicalName();
   }

   @Override
   public Set<Modifier> getModifiers()
   {
      int modifiers = getModifiersAsInt();
      return ReflectionUtil.getModifiers(modifiers, false);
   }

   private int getModifiersAsInt()
   {
      TypeKind typeKind = getTypeKind();

      if (typeKind.equals(TypeKind.INTERFACE) || typeKind.equals(TypeKind.ANNOTATION))
      {
         return getaClass().getModifiers() & java.lang.reflect.Modifier.interfaceModifiers();
      }
      return getaClass().getModifiers() & java.lang.reflect.Modifier.classModifiers();
   }

   @Override
   public boolean isSubtypeOf(Shadow shadow)
   {
      if (equals(shadow))
      {
         return true;
      }

      return Converter.convert(shadow).toDeclared().map(declared -> getSuperTypes().contains(declared)).orElse(false);
   }

   @Override
   public NestingKind getNesting()
   {
      if (getaClass().isAnonymousClass() || getaClass().isLocalClass() || getaClass().isMemberClass())
      {
         return NestingKind.INNER;
      }
      return NestingKind.OUTER;
   }

   @Override
   public List<Field> getFields()
   {
      return stream(getaClass().getDeclaredFields()).map(ReflectionAdapter::getShadow)
                                                    .collect(collectingAndThen(toList(), Collections::unmodifiableList));
   }

   @Override
   public List<Method> getMethods()
   {
      return stream(getaClass().getDeclaredMethods()).map(ReflectionAdapter::getShadow)
                                                     .collect(collectingAndThen(toList(), Collections::unmodifiableList));
   }

   @Override
   public List<Constructor> getConstructors()
   {
      return stream(getaClass().getDeclaredConstructors()).map(ReflectionAdapter::getShadow)
                                                          .collect(collectingAndThen(toList(), Collections::unmodifiableList));
   }

   @Override
   public List<Declared> getDirectSuperTypes()
   {
      List<Declared> result = stream(getaClass().getGenericInterfaces())
            .map(ReflectionAdapter::getShadow)
            .map(Converter::convert)
            .map(ShadowConverter::toDeclaredOrThrow)
            .collect(Collectors.toList());

      ofNullable(getaClass().getGenericSuperclass())
            .map(ReflectionAdapter::getShadow)
            .map(Converter::convert)
            .map(ShadowConverter::toDeclaredOrThrow)
            .ifPresent(result::add);

      if (result.isEmpty() && isTypeKind(TypeKind.INTERFACE))
      {
         result.add(new ClassImpl(Object.class));
      }
      return result;
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
   public List<Interface> getInterfaces()
   {
      return getSuperTypes().stream()
                            .filter(declared -> declared.getTypeKind().equals(TypeKind.INTERFACE))
                            .map(Converter::convert)
                            .map(DeclaredConverter::toInterfaceOrThrow)
                            .collect(collectingAndThen(toList(), Collections::unmodifiableList));
   }

   @Override
   public List<Interface> getDirectInterfaces()
   {
      return stream(getaClass().getInterfaces()).map(ReflectionAdapter::getShadow)
                                                .map(Interface.class::cast)
                                                .collect(collectingAndThen(toList(), Collections::unmodifiableList));
   }

   @Override
   public Package getPackage()
   {
      String name = getaClass().getName();
      int i = name.lastIndexOf('.');
      if (i != -1)
      {
         return ReflectionAdapter.getPackageShadow(name.substring(0, i));
      }
      return null;
   }

   @Override
   public String getBinaryName()
   {
      return getaClass().getName();
   }

   @Override
   public List<EnumConstant> getEumConstants()
   {
      return stream(getaClass().getEnumConstants())
            .map(java.lang.Enum.class::cast)
            .map(ReflectionAdapter::getShadow)
            .collect(collectingAndThen(toList(), Collections::unmodifiableList));
   }

   @Override
   public TypeKind getTypeKind()
   {
      if (getaClass().isEnum())
      {
         return TypeKind.ENUM;
      }
      //care. an annotation is a interface
      if (getaClass().isAnnotation())
      {
         return TypeKind.ANNOTATION;
      }
      if (getaClass().isInterface())
      {
         return TypeKind.INTERFACE;
      }
      return TypeKind.CLASS;
   }

   @Override
   public boolean representsSameType(Shadow shadow)
   {
      return shadow != null && shadow.getTypeKind().equals(getTypeKind());
   }

   public Class<?> getaClass()
   {
      return aClass;
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getTypeKind(),
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
      if (!(other instanceof Declared))
      {
         return false;
      }
      Declared otherDeclared = ((Declared) other);
      return Objects.equals(getQualifiedName(), otherDeclared.getQualifiedName()) &&
             Objects.equals(getTypeKind(), otherDeclared.getTypeKind()) &&
             Objects.equals(getModifiers(), otherDeclared.getModifiers());
   }

   public Class<?> getReflection()
   {
      return aClass;
   }
}
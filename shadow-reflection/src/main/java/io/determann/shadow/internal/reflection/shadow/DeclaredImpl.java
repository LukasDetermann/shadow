package io.determann.shadow.internal.reflection.shadow;

import io.determann.shadow.api.NestingKind;
import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.converter.Converter;
import io.determann.shadow.api.converter.DeclaredConverter;
import io.determann.shadow.api.converter.TypeConverter;
import io.determann.shadow.api.modifier.Modifier;
import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.shadow.Enum;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.*;
import io.determann.shadow.internal.reflection.ReflectionUtil;

import java.lang.Class;
import java.util.*;
import java.util.stream.Collectors;

import static io.determann.shadow.api.converter.Converter.convert;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;

public class DeclaredImpl implements Annotation,
                                     Enum
{
   private final Class<?> aClass;

   public DeclaredImpl(Class<?> aClass)
   {
      this.aClass = aClass;
   }

   @Override
   public Module getModule()
   {
      return ReflectionAdapter.generalize(getaClass().getModule());
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
            .map(ReflectionAdapter::generalize)
            .toList();
   }

   @Override
   public List<AnnotationUsage> getDirectAnnotationUsages()
   {
      return stream(getaClass().getDeclaredAnnotations())
            .map(ReflectionAdapter::generalize)
            .toList();
   }

   @Override
   public String getQualifiedName()
   {
      return getaClass().getCanonicalName();
   }

   @Override
   public Set<Modifier> getModifiers()
   {
      boolean isSealed = getaClass().isSealed();
      int modifiers = getModifiersAsInt();
      return ReflectionUtil.getModifiers(modifiers, isSealed, false);
   }

   private int getModifiersAsInt()
   {
      TypeKind typeKind = getKind();

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

      return convert(shadow).toDeclared().map(declared -> getSuperTypes().contains(declared)).orElse(false);
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
      return stream(getaClass().getDeclaredFields()).map(ReflectionAdapter::generalize).toList();
   }

   @Override
   public List<Method> getMethods()
   {
      return stream(getaClass().getDeclaredMethods()).map(ReflectionAdapter::generalize).toList();
   }

   @Override
   public List<Constructor> getConstructors()
   {
      return stream(getaClass().getDeclaredConstructors()).map(ReflectionAdapter::generalize).toList();
   }

   @Override
   public List<Declared> getDirectSuperTypes()
   {
      List<Declared> result = stream(getaClass().getGenericInterfaces())
            .map(ReflectionAdapter::generalize)
            .map(Converter::convert)
            .map(TypeConverter::toDeclaredOrThrow)
            .collect(Collectors.toList());

      ofNullable(getaClass().getGenericSuperclass())
            .map(ReflectionAdapter::generalize)
            .map(Converter::convert)
            .map(TypeConverter::toDeclaredOrThrow)
            .ifPresent(result::add);

      if (result.isEmpty() && isKind(TypeKind.INTERFACE))
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
                            .filter(declared -> declared.getKind().equals(TypeKind.INTERFACE))
                            .map(Converter::convert)
                            .map(DeclaredConverter::toInterfaceOrThrow)
                            .toList();
   }

   @Override
   public List<Interface> getDirectInterfaces()
   {
      return stream(getaClass().getInterfaces()).map(ReflectionAdapter::generalize).map(Interface.class::cast).toList();
   }

   @Override
   public Package getPackage()
   {
      return ReflectionAdapter.generalize(getaClass().getPackage());
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
            .map(ReflectionAdapter::generalize)
            .toList();
   }

   @Override
   public TypeKind getKind()
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
      if (getaClass().isRecord())
      {
         return TypeKind.RECORD;
      }
      return TypeKind.CLASS;
   }

   @Override
   public boolean representsSameType(Shadow shadow)
   {
      return shadow != null && (equals(shadow) || sameGenerics(shadow));
   }

   private boolean sameGenerics(Shadow shadow)
   {
      if (!getKind().equals(shadow.getKind()))
      {
         return false;
      }
      if (isKind(TypeKind.ENUM) ||
          isKind(TypeKind.ANNOTATION))
      {
         return false;
      }

      if (isKind(TypeKind.CLASS))
      {
         return sameGenerics(convert(((Declared) this)).toClassOrThrow().getGenerics(), convert(shadow).toClassOrThrow().getGenerics());
      }
      if (isKind(TypeKind.INTERFACE))
      {
         return sameGenerics(convert(((Declared) this)).toInterfaceOrThrow().getGenerics(), convert(shadow).toInterfaceOrThrow().getGenerics());
      }
      if (isKind(TypeKind.RECORD))
      {
         return sameGenerics(convert(((Declared) this)).toRecordOrThrow().getGenerics(), convert(shadow).toRecordOrThrow().getGenerics());
      }
      throw new IllegalStateException();
   }

   private boolean sameGenerics(List<Generic> generics, List<Generic> generics1)
   {
      if (generics.size() != generics1.size())
      {
         return false;
      }

      Iterator<Generic> iterator = generics.iterator();
      Iterator<Generic> iterator1 = generics1.iterator();
      while (iterator.hasNext() && iterator1.hasNext())
      {
         if (!iterator.next().representsSameType(iterator1.next()))
         {
            return false;
         }
      }
      return true;
   }

   public Class<?> getaClass()
   {
      return aClass;
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
      return Objects.equals(getQualifiedName(), otherDeclared.getQualifiedName()) &&
             Objects.equals(getKind(), otherDeclared.getKind()) &&
             Objects.equals(getModifiers(), otherDeclared.getModifiers());
   }

   public Class<?> getReflection()
   {
      return aClass;
   }
}

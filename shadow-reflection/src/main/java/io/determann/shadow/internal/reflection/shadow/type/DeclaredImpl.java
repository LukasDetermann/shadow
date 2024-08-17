package io.determann.shadow.internal.reflection.shadow.type;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.reflection.shadow.NameableReflection;
import io.determann.shadow.api.reflection.shadow.QualifiedNameableReflection;
import io.determann.shadow.api.reflection.shadow.structure.ModuleEnclosedReflection;
import io.determann.shadow.api.reflection.shadow.type.DeclaredReflection;
import io.determann.shadow.api.reflection.shadow.type.ShadowReflection;
import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.NestingKind;
import io.determann.shadow.api.shadow.TypeKind;
import io.determann.shadow.api.shadow.modifier.Modifier;
import io.determann.shadow.api.shadow.structure.Module;
import io.determann.shadow.api.shadow.structure.Package;
import io.determann.shadow.api.shadow.structure.*;
import io.determann.shadow.api.shadow.type.Declared;
import io.determann.shadow.api.shadow.type.Generic;
import io.determann.shadow.api.shadow.type.Interface;
import io.determann.shadow.api.shadow.type.Shadow;
import io.determann.shadow.internal.reflection.ReflectionUtil;

import java.util.*;
import java.util.stream.Collectors;

import static io.determann.shadow.api.shadow.Operations.*;
import static io.determann.shadow.api.shadow.Provider.requestOrEmpty;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;
import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;

public abstract class DeclaredImpl implements DeclaredReflection,
                                     NameableReflection,
                                     ShadowReflection,
                                     QualifiedNameableReflection,
                                     ModuleEnclosedReflection
{
   private final Class<?> aClass;

   protected DeclaredImpl(Class<?> aClass)
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
      boolean isNonSealed = isNonSealed(modifiers);

      boolean isPackagePrivate = !java.lang.reflect.Modifier.isPublic(modifiers) &&
                                 !java.lang.reflect.Modifier.isPrivate(modifiers) &&
                                 !java.lang.reflect.Modifier.isProtected(modifiers);

      return ReflectionUtil.getModifiers(modifiers, isSealed, isNonSealed, false, isPackagePrivate);
   }

   private boolean isNonSealed(int modifiers)
   {
      return !java.lang.reflect.Modifier.isFinal(modifiers) &&
             !getReflection().isSealed() &&
             (ofNullable(getReflection().getSuperclass()).map(Class::isSealed).orElse(false) ||
              stream(getReflection().getInterfaces()).anyMatch(Class::isSealed));
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
      return equals(shadow) || shadow instanceof Declared declared && getSuperTypes().contains(declared);
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
            .map(Declared.class::cast)
            .collect(Collectors.toList());

      ofNullable(getaClass().getGenericSuperclass())
            .map(ReflectionAdapter::generalize)
            .map(Declared.class::cast)
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
      List<? extends Declared> directSupertypes = requestOrThrow(declared, DECLARED_GET_DIRECT_SUPER_TYPES);
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
                            .filter(declared -> TypeKind.INTERFACE.equals(requestOrThrow(declared, SHADOW_GET_KIND)))
                            .map(Interface.class::cast)
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
         if (!requestOrThrow(iterator.next(), SHADOW_REPRESENTS_SAME_TYPE, iterator1.next()))
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
      return Objects.equals(getQualifiedName(), requestOrThrow(otherDeclared, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME)) &&
             Objects.equals(getKind(), requestOrThrow(otherDeclared, SHADOW_GET_KIND)) &&
             requestOrEmpty(otherDeclared, MODIFIABLE_GET_MODIFIERS).map(modifiers -> Objects.equals(modifiers, getModifiers())).orElse(false);
   }

   public Class<?> getReflection()
   {
      return aClass;
   }

   @Override
   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
   }
}

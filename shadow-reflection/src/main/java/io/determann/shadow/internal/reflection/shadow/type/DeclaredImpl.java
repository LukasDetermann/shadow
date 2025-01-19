package io.determann.shadow.internal.reflection.shadow.type;

import io.determann.shadow.api.Implementation;
import io.determann.shadow.api.reflection.R_Adapter;
import io.determann.shadow.api.reflection.shadow.R_AnnotationUsage;
import io.determann.shadow.api.reflection.shadow.structure.*;
import io.determann.shadow.api.reflection.shadow.type.R_Array;
import io.determann.shadow.api.reflection.shadow.type.R_Declared;
import io.determann.shadow.api.reflection.shadow.type.R_Interface;
import io.determann.shadow.api.shadow.C_NestingKind;
import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.type.*;
import io.determann.shadow.internal.reflection.ReflectionUtil;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.reflection.R_Adapter.IMPLEMENTATION;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;

public abstract class DeclaredImpl
{
   private final Class<?> aClass;

   protected DeclaredImpl(Class<?> aClass)
   {
      this.aClass = aClass;
   }

   public R_Module getModule()
   {
      return R_Adapter.generalize(getaClass().getModule());
   }

   public String getName()
   {
      return getaClass().getSimpleName();
   }

   public List<R_AnnotationUsage> getAnnotationUsages()
   {
      return stream(getaClass().getAnnotations())
            .map(R_Adapter::generalize)
            .toList();
   }

   public List<R_AnnotationUsage> getDirectAnnotationUsages()
   {
      return stream(getaClass().getDeclaredAnnotations())
            .map(R_Adapter::generalize)
            .toList();
   }

   public String getQualifiedName()
   {
      return getaClass().getCanonicalName();
   }

   public Set<C_Modifier> getModifiers()
   {
      boolean isSealed = getaClass().isSealed();
      int modifiers = getModifiersAsInt();
      boolean isNonSealed = isNonSealed(modifiers);

      boolean isPackagePrivate = !Modifier.isPublic(modifiers) &&
                                 !Modifier.isPrivate(modifiers) &&
                                 !Modifier.isProtected(modifiers);

      return ReflectionUtil.getModifiers(modifiers, isSealed, isNonSealed, false, isPackagePrivate);
   }

   private boolean isNonSealed(int modifiers)
   {
      return !Modifier.isFinal(modifiers) &&
             !getReflection().isSealed() &&
             (ofNullable(getReflection().getSuperclass()).map(Class::isSealed).orElse(false) ||
              stream(getReflection().getInterfaces()).anyMatch(Class::isSealed));
   }

   private int getModifiersAsInt()
   {
      if (this instanceof C_Interface || this instanceof C_Annotation)
      {
         return getaClass().getModifiers() & Modifier.interfaceModifiers();
      }
      return getaClass().getModifiers() & Modifier.classModifiers();
   }

   public boolean isSubtypeOf(C_Type type)
   {
      return equals(type) || type instanceof C_Declared declared && getSuperTypes().contains(declared);
   }

   public C_NestingKind getNesting()
   {
      if (getaClass().isAnonymousClass() || getaClass().isLocalClass() || getaClass().isMemberClass())
      {
         return C_NestingKind.INNER;
      }
      return C_NestingKind.OUTER;
   }

   public List<R_Field> getFields()
   {
      return stream(getaClass().getDeclaredFields()).map(R_Adapter::generalize).toList();
   }

   public List<R_Method> getMethods()
   {
      return stream(getaClass().getDeclaredMethods()).map(R_Adapter::generalize).toList();
   }

   public List<R_Constructor> getConstructors()
   {
      return stream(getaClass().getDeclaredConstructors()).map(R_Adapter::generalize).toList();
   }

   public List<R_Declared> getDirectSuperTypes()
   {
      List<R_Declared> result = stream(getaClass().getGenericInterfaces())
            .map(R_Adapter::generalize)
            .map(R_Declared.class::cast)
            .collect(Collectors.toList());

      ofNullable(getaClass().getGenericSuperclass())
            .map(R_Adapter::generalize)
            .map(R_Declared.class::cast)
            .ifPresent(result::add);

      if (result.isEmpty() && this instanceof R_Interface)
      {
         result.add(new ClassImpl(Object.class));
      }
      return result;
   }

   public Set<R_Declared> getSuperTypes()
   {
      return findAllSupertypes(new HashSet<>(), ((R_Declared) this));
   }

   private Set<R_Declared> findAllSupertypes(Set<R_Declared> found, R_Declared declared)
   {
      List<R_Declared> directSupertypes = declared.getDirectSuperTypes();
      found.addAll(directSupertypes);
      for (R_Declared directSupertype : directSupertypes)
      {
         findAllSupertypes(found, directSupertype);
      }
      return found;
   }

   public List<R_Interface> getInterfaces()
   {
      return getSuperTypes().stream()
                            .filter(declared -> declared instanceof R_Interface)
                            .map(R_Interface.class::cast)
                            .toList();
   }

   public List<R_Interface> getDirectInterfaces()
   {
      return stream(getaClass().getInterfaces()).map(R_Adapter::generalize).map(R_Interface.class::cast).toList();
   }

   public R_Package getPackage()
   {
      return R_Adapter.generalize(getaClass().getPackage());
   }

   public String getBinaryName()
   {
      return getaClass().getName();
   }

   public R_Array asArray()
   {
      return R_Adapter.generalize(aClass.arrayType());
   }

   private boolean sameGenerics(List<C_Generic> generics, List<C_Generic> generics1)
   {
      if (generics.size() != generics1.size())
      {
         return false;
      }

      Iterator<C_Generic> iterator = generics.iterator();
      Iterator<C_Generic> iterator1 = generics1.iterator();
      while (iterator.hasNext() && iterator1.hasNext())
      {
         if (!requestOrThrow(iterator.next(), TYPE_REPRESENTS_SAME_TYPE, iterator1.next()))
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

   public Class<?> getReflection()
   {
      return aClass;
   }

   public Implementation getImplementation()
   {
      return IMPLEMENTATION;
   }
}

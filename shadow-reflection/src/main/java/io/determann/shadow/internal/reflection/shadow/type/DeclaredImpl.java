package io.determann.shadow.internal.reflection.shadow.type;

import io.determann.shadow.api.C;
import io.determann.shadow.api.Modifier;
import io.determann.shadow.api.NestingKind;
import io.determann.shadow.api.query.Implementation;
import io.determann.shadow.api.reflection.Adapter;
import io.determann.shadow.api.reflection.R;
import io.determann.shadow.internal.reflection.ReflectionUtil;

import java.util.*;
import java.util.stream.Collectors;

import static io.determann.shadow.api.query.Operations.*;
import static io.determann.shadow.api.query.Provider.requestOrEmpty;
import static io.determann.shadow.api.query.Provider.requestOrThrow;
import static io.determann.shadow.api.reflection.Adapter.IMPLEMENTATION;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;

public abstract class DeclaredImpl
{
   private final Class<?> aClass;

   protected DeclaredImpl(Class<?> aClass)
   {
      this.aClass = aClass;
   }

   public R.Module getModule()
   {
      return Adapter.generalize(getaClass().getModule());
   }

   public String getName()
   {
      return getaClass().getSimpleName();
   }

   public List<R.AnnotationUsage> getAnnotationUsages()
   {
      return stream(getaClass().getAnnotations())
            .map(Adapter::generalize)
            .toList();
   }

   public List<R.AnnotationUsage> getDirectAnnotationUsages()
   {
      return stream(getaClass().getDeclaredAnnotations())
            .map(Adapter::generalize)
            .toList();
   }

   public String getQualifiedName()
   {
      return getaClass().getCanonicalName();
   }

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
      if (this instanceof C.Interface || this instanceof C.Annotation)
      {
         return getaClass().getModifiers() & java.lang.reflect.Modifier.interfaceModifiers();
      }
      return getaClass().getModifiers() & java.lang.reflect.Modifier.classModifiers();
   }

   public boolean isSubtypeOf(C.Type type)
   {
      return equals(type) || type instanceof C.Declared declared && getSuperTypes().contains(declared);
   }

   public NestingKind getNesting()
   {
      if (getaClass().isAnonymousClass() || getaClass().isLocalClass() || getaClass().isMemberClass())
      {
         return NestingKind.INNER;
      }
      return NestingKind.OUTER;
   }

   public List<R.Field> getFields()
   {
      return stream(getaClass().getDeclaredFields()).map(Adapter::generalize).toList();
   }

   public List<R.Method> getMethods()
   {
      return stream(getaClass().getDeclaredMethods()).map(Adapter::generalize).toList();
   }

   public List<R.Constructor> getConstructors()
   {
      return stream(getaClass().getDeclaredConstructors()).map(Adapter::generalize).toList();
   }

   public List<R.Declared> getDirectSuperTypes()
   {
      List<R.Declared> result = stream(getaClass().getGenericInterfaces())
            .map(Adapter::generalize)
            .map(R.Declared.class::cast)
            .collect(Collectors.toList());

      ofNullable(getaClass().getGenericSuperclass())
            .map(Adapter::generalize)
            .map(R.Declared.class::cast)
            .ifPresent(result::add);

      if (result.isEmpty() && this instanceof R.Interface)
      {
         result.add(new ClassImpl(Object.class));
      }
      return result;
   }

   public Set<R.Declared> getSuperTypes()
   {
      return findAllSupertypes(new HashSet<>(), ((R.Declared) this));
   }

   private Set<R.Declared> findAllSupertypes(Set<R.Declared> found, R.Declared declared)
   {
      List<R.Declared> directSupertypes = declared.getDirectSuperTypes();
      found.addAll(directSupertypes);
      for (R.Declared directSupertype : directSupertypes)
      {
         findAllSupertypes(found, directSupertype);
      }
      return found;
   }

   public List<R.Interface> getInterfaces()
   {
      return getSuperTypes().stream()
                            .filter(declared -> declared instanceof R.Interface)
                            .map(R.Interface.class::cast)
                            .toList();
   }

   public List<R.Interface> getDirectInterfaces()
   {
      return stream(getaClass().getInterfaces()).map(Adapter::generalize).map(R.Interface.class::cast).toList();
   }

   public R.Package getPackage()
   {
      return Adapter.generalize(getaClass().getPackage());
   }

   public String getBinaryName()
   {
      return getaClass().getName();
   }

   public R.Array asArray()
   {
      return Adapter.generalize(aClass.arrayType());
   }

   private boolean sameGenerics(List<C.Generic> generics, List<C.Generic> generics1)
   {
      if (generics.size() != generics1.size())
      {
         return false;
      }

      Iterator<C.Generic> iterator = generics.iterator();
      Iterator<C.Generic> iterator1 = generics1.iterator();
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
      if (!(other instanceof C.Declared otherDeclared))
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

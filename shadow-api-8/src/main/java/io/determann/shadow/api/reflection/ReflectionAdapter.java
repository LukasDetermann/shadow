package io.determann.shadow.api.reflection;

import io.determann.shadow.api.shadow.Array;
import io.determann.shadow.api.shadow.Constructor;
import io.determann.shadow.api.shadow.Executable;
import io.determann.shadow.api.shadow.Field;
import io.determann.shadow.api.shadow.Method;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.Parameter;
import io.determann.shadow.api.shadow.*;
import io.determann.shadow.impl.reflection.NamedSupplier;
import io.determann.shadow.impl.reflection.shadow.*;

import java.lang.Class;
import java.lang.Enum;
import java.lang.Void;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public class ReflectionAdapter
{
   public static <SHADOW extends Shadow> SHADOW getShadow(Class<?> aClass)
   {
      return getShadow(aClass, Collections.emptyList());
   }

   @SuppressWarnings("unchecked")
   private static <SHADOW extends Shadow> SHADOW getShadow(Class<?> aClass, List<Shadow> genericTypes)
   {
      if (aClass.isPrimitive())
      {
         if (aClass.equals(Void.TYPE))
         {
            return (SHADOW) new VoidImpl();
         }
         return (SHADOW) new PrimitiveImpl(aClass);
      }
      if (aClass.isArray())
      {
         return (SHADOW) new ArrayImpl(aClass);
      }
      if (aClass.isAnnotation() || aClass.isEnum())
      {
         return (SHADOW) new DeclaredImpl(aClass);
      }
      if (aClass.isInterface())
      {
         return (SHADOW) new InterfaceImpl(aClass, genericTypes);
      }
      return (SHADOW) new ClassImpl(aClass, genericTypes);
   }

   public static Package getShadow(java.lang.Package aPackage)
   {
      return new PackageImpl(new NamedSupplier<>(aPackage, java.lang.Package::getName));
   }

   public static Shadow getShadow(Type type)
   {
      if (type instanceof ParameterizedType)
      {
         ParameterizedType parameterizedType = ((ParameterizedType) type);
         Type rawType = parameterizedType.getRawType();
         if (rawType instanceof Class<?>)
         {
            Class<?> aClass = ((Class<?>) rawType);
            return getShadow(aClass,
                             Arrays.stream(parameterizedType.getActualTypeArguments())
                                   .map(ReflectionAdapter::getShadow)
                                   .collect(collectingAndThen(toList(), Collections::unmodifiableList)));
         }
         throw new IllegalStateException();
      }
      if (type instanceof TypeVariable<?>)
      {
         TypeVariable<?> typeVariable = ((TypeVariable<?>) type);
         return new GenericImpl(typeVariable);
      }
      if (type instanceof Class<?>)
      {
         Class<?> aClass = ((Class<?>) type);
         return getShadow(aClass);
      }
      if (type instanceof WildcardType)
      {
         WildcardType wildcardType = ((WildcardType) type);
         return new WildcardImpl(wildcardType);
      }
      throw new IllegalStateException();
   }

   public static AnnotationUsage getAnnotationUsage(java.lang.annotation.Annotation annotation)
   {
      return new AnnotationUsageImpl(annotation);
   }

   public static Field getShadow(java.lang.reflect.Field field)
   {
      return new FieldImpl(field);
   }

   public static Method getShadow(java.lang.reflect.Method method)
   {
      return new ExecutableImpl(method);
   }

   public static Constructor getShadow(java.lang.reflect.Constructor<?> constructor)
   {
      return new ExecutableImpl(constructor);
   }

   public static Parameter getShadow(java.lang.reflect.Parameter parameter)
   {
      return new ParameterImpl(parameter);
   }

   public static Shadow getShadow(TypeVariable<?> typeVariable)
   {
      return new GenericImpl(typeVariable);
   }

   public static Optional<Declared> getDeclaredShadow(String qualifiedName)
   {
      try
      {
         Class<?> aClass = Class.forName(qualifiedName);
         return Optional.of(getShadow(aClass));
      }
      catch (ClassNotFoundException e)
      {
         return Optional.empty();
      }
   }

   public static Executable getShadow(java.lang.reflect.Executable executable)
   {
      return new ExecutableImpl(executable);
   }

   public static Package getPackageShadow(String name)
   {
      return new PackageImpl(new NamedSupplier<>(name, () ->
      {
         java.lang.Package aPackage = java.lang.Package.getPackage(name);
         if (aPackage == null)
         {
            throw new IllegalArgumentException("Package \"" + name + "\" not found. The VM did not load it yet");
         }
         return aPackage;
      }, java.lang.Package::getName));
   }

   public static EnumConstant getShadow(Enum<?> enumConstant)
   {
      try
      {
         return new EnumConstantImpl(enumConstant.getDeclaringClass().getField(enumConstant.name()));
      }
      catch (NoSuchFieldException e)
      {
         throw new RuntimeException(e);
      }
   }

   public static Shadow getShadow(GenericDeclaration genericDeclaration)
   {
      if (genericDeclaration instanceof Class<?>)
      {
         Class<?> aClass = ((Class<?>) genericDeclaration);
         return getShadow(aClass);
      }
      if (genericDeclaration instanceof java.lang.reflect.Executable)
      {
         java.lang.reflect.Executable executable = ((java.lang.reflect.Executable) genericDeclaration);
         return getShadow(executable);
      }
      throw new IllegalStateException();
   }

   public static java.lang.annotation.Annotation getReflection(AnnotationUsage annotationUsage)
   {
      return ((AnnotationUsageImpl) annotationUsage).getAnnotationReflection();
   }

   public static Class<?> getReflection(Array array)
   {
      return ((ArrayImpl) array).getReflection();
   }

   public static Class<?> getReflection(Declared declared)
   {
      return ((DeclaredImpl) declared).getReflection();
   }

   public static java.lang.reflect.Field getReflection(EnumConstant enumConstant)
   {
      return ((ReflectionFieldImpl<?>) enumConstant).getReflection();
   }

   public static java.lang.reflect.Executable getReflection(Executable executable)
   {
      return ((ExecutableImpl) executable).getReflection();
   }

   public static java.lang.reflect.Field getReflection(Field field)
   {
      return ((ReflectionFieldImpl<?>) field).getReflection();
   }

   public static TypeVariable<?> getReflection(Generic generic)
   {
      return ((GenericImpl) generic).getReflection();
   }

   public static Type[] getReflection(Intersection intersection)
   {
      return ((IntersectionImpl) intersection).getReflection();
   }

   public static java.lang.Package getReflection(Package aPackage)
   {
      return ((PackageImpl) aPackage).getReflection();
   }

   public static java.lang.reflect.Parameter getReflection(Parameter parameter)
   {
      return ((ParameterImpl) parameter).getReflection();
   }

   public static Class<?> getReflection(Primitive primitive)
   {
      return ((PrimitiveImpl) primitive).getReflection();
   }

   public static WildcardType getReflection(Wildcard wildcard)
   {
      return ((WildcardImpl) wildcard).getReflection();
   }
}
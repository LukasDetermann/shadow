package io.determann.shadow.api.reflection;

import io.determann.shadow.api.shadow.Array;
import io.determann.shadow.api.shadow.Constructor;
import io.determann.shadow.api.shadow.Executable;
import io.determann.shadow.api.shadow.Field;
import io.determann.shadow.api.shadow.Method;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.Parameter;
import io.determann.shadow.api.shadow.RecordComponent;
import io.determann.shadow.api.shadow.*;
import io.determann.shadow.api.shadow.module.*;
import io.determann.shadow.internal.reflection.NamedSupplier;
import io.determann.shadow.internal.reflection.shadow.*;
import io.determann.shadow.internal.reflection.shadow.module.*;

import java.lang.Class;
import java.lang.Enum;
import java.lang.Void;
import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
      if (aClass.isRecord())
      {
         return (SHADOW) new RecordImpl(aClass, genericTypes);
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

   public static Module getShadow(java.lang.Module module)
   {
      return new ModuleImpl(new NamedSupplier<>(module.getDescriptor(), ModuleDescriptor::name),
                            Arrays.stream(module.getAnnotations()).map(ReflectionAdapter::getAnnotationUsage).toList());
   }

   public static Module getModuleShadow(String name)
   {
      return new ModuleImpl(new NamedSupplier<>(name,
                                                () -> ModuleFinder.ofSystem().find(name).orElseThrow().descriptor(),
                                                ModuleDescriptor::name));
   }

   public static Module getShadow(ModuleReference moduleReference)
   {
      return new ModuleImpl(new NamedSupplier<>(moduleReference.descriptor(), ModuleDescriptor::name));
   }

   public static Shadow getShadow(Type type)
   {
      if (type instanceof ParameterizedType parameterizedType)
      {
         Type rawType = parameterizedType.getRawType();
         if (rawType instanceof Class<?> aClass)
         {
            return getShadow(aClass, Arrays.stream(parameterizedType.getActualTypeArguments()).map(ReflectionAdapter::getShadow).toList());
         }
         throw new IllegalStateException();
      }
      if (type instanceof TypeVariable<?> typeVariable)
      {
         return new GenericImpl(typeVariable);
      }
      if (type instanceof Class<?> aClass)
      {
         return getShadow(aClass);
      }
      if (type instanceof WildcardType wildcardType)
      {
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

   public static RecordComponent getShadow(java.lang.reflect.RecordComponent recordComponent)
   {
      return new RecordComponentImpl(recordComponent);
   }

   public static Parameter getShadow(java.lang.reflect.Parameter parameter)
   {
      return new ParameterImpl(parameter);
   }

   public static Shadow getShadow(TypeVariable<?> typeVariable)
   {
      return new GenericImpl(typeVariable);
   }

   public static Requires getShadow(ModuleDescriptor.Requires requires)
   {
      return new RequiresImpl(requires);
   }

   public static Exports getShadow(ModuleDescriptor.Exports exports)
   {
      return new ExportsImpl(exports);
   }

   public static Opens getShadow(ModuleDescriptor.Opens opens)
   {
      return new OpensImpl(opens);
   }

   public static Provides getShadow(ModuleDescriptor.Provides provides)
   {
      return new ProvidesImpl(provides);
   }

   public static Uses getUsesShadow(String uses)
   {
      return new UsesImpl(uses);
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
      if (genericDeclaration instanceof Class<?> aClass)
      {
         return getShadow(aClass);
      }
      if (genericDeclaration instanceof java.lang.reflect.Executable executable)
      {
         return getShadow(executable);
      }
      throw new IllegalStateException();
   }

   public static ModuleDescriptor.Exports getReflection(Exports exports)
   {
      return ((ExportsImpl) exports).getReflection();
   }

   public static ModuleDescriptor.Opens getReflection(Opens opens)
   {
      return ((OpensImpl) opens).getReflection();
   }

   public static ModuleDescriptor.Provides getReflection(Provides provides)
   {
      return ((ProvidesImpl) provides).getReflection();
   }

   public static ModuleDescriptor.Requires getReflection(Requires requires)
   {
      return ((RequiresImpl) requires).getReflection();
   }

   public static String getReflection(Uses uses)
   {
      return ((UsesImpl) uses).getReflection();
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

   public static ModuleDescriptor getReflection(Module module)
   {
      return ((ModuleImpl) module).getReflection();
   }

   public static java.lang.Package getReflection(Package aPackage)
   {
      return ((PackageImpl) aPackage).getReflection();
   }

   public static java.lang.reflect.Parameter getReflection(Parameter parameter)
   {
      return ((ParameterImpl) parameter).getReflection();
   }

   public static java.lang.Class<?> getReflection(Primitive primitive)
   {
      return ((PrimitiveImpl) primitive).getReflection();
   }

   public static java.lang.reflect.RecordComponent getReflection(RecordComponent recordComponent)
   {
      return ((RecordComponentImpl) recordComponent).getReflection();
   }

   public static WildcardType getReflection(Wildcard wildcard)
   {
      return ((WildcardImpl) wildcard).getReflection();
   }
}
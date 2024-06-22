package io.determann.shadow.api.reflection;

import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
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
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface ReflectionAdapter
{
   public static <SHADOW extends Shadow> SHADOW generalize(Class<?> aClass)
   {
      return generalize(aClass, Collections.emptyList());
   }

   @SuppressWarnings("unchecked")
   private static <SHADOW extends Shadow> SHADOW generalize(Class<?> aClass, List<Shadow> genericShadows)
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
         return (SHADOW) new RecordImpl(aClass, genericShadows);
      }
      if (aClass.isAnnotation() || aClass.isEnum())
      {
         return (SHADOW) new DeclaredImpl(aClass);
      }
      if (aClass.isInterface())
      {
         return (SHADOW) new InterfaceImpl(aClass, genericShadows);
      }
      return (SHADOW) new ClassImpl(aClass, genericShadows);
   }

   public static Package generalize(java.lang.Package aPackage)
   {
      return new PackageImpl(new NamedSupplier<>(aPackage, java.lang.Package::getName));
   }

   public static Module generalize(java.lang.Module module)
   {
      return new ModuleImpl(new NamedSupplier<>(module.getDescriptor(), ModuleDescriptor::name),
                            Arrays.stream(module.getAnnotations()).map(ReflectionAdapter::generalize).toList());
   }

   public static Module getModuleShadow(String name)
   {
      return new ModuleImpl(new NamedSupplier<>(name,
                                                () -> ModuleFinder.ofSystem().find(name).orElseThrow().descriptor(),
                                                ModuleDescriptor::name));
   }

   public static Module generalize(ModuleReference moduleReference)
   {
      return new ModuleImpl(new NamedSupplier<>(moduleReference.descriptor(), ModuleDescriptor::name));
   }

   public static Shadow generalize(java.lang.reflect.Type type)
   {
      if (type instanceof ParameterizedType parameterizedType)
      {
         java.lang.reflect.Type rawType = parameterizedType.getRawType();
         if (rawType instanceof Class<?> aClass)
         {
            return generalize(aClass, Arrays.stream(parameterizedType.getActualTypeArguments()).map(ReflectionAdapter::generalize).toList());
         }
         throw new IllegalStateException();
      }
      if (type instanceof TypeVariable<?> typeVariable)
      {
         return new GenericImpl(typeVariable);
      }
      if (type instanceof Class<?> aClass)
      {
         return generalize(aClass);
      }
      if (type instanceof WildcardType wildcardType)
      {
         return new WildcardImpl(wildcardType);
      }
      throw new IllegalStateException();
   }

   public static AnnotationUsage generalize(java.lang.annotation.Annotation annotation)
   {
      return new AnnotationUsageImpl(annotation);
   }

   public static Field generalize(java.lang.reflect.Field field)
   {
      return new FieldImpl(field);
   }

   public static Method generalize(java.lang.reflect.Method method)
   {
      return new ExecutableImpl(method);
   }

   public static Constructor generalize(java.lang.reflect.Constructor<?> constructor)
   {
      return new ExecutableImpl(constructor);
   }

   public static RecordComponent generalize(java.lang.reflect.RecordComponent recordComponent)
   {
      return (RecordComponent) new RecordComponentImpl(recordComponent);
   }

   public static Parameter generalize(java.lang.reflect.Parameter parameter)
   {
      return new ParameterImpl(parameter);
   }

   public static Shadow generalize(TypeVariable<?> typeVariable)
   {
      return new GenericImpl(typeVariable);
   }

   public static Requires generalize(ModuleDescriptor.Requires requires)
   {
      return new RequiresImpl(requires);
   }

   public static Exports generalize(ModuleDescriptor.Exports exports)
   {
      return new ExportsImpl(exports);
   }

   public static Opens generalize(ModuleDescriptor.Opens opens)
   {
      return new OpensImpl(opens);
   }

   public static Provides generalize(ModuleDescriptor.Provides provides)
   {
      return new ProvidesImpl(provides);
   }

   public static Uses getUsesShadow(String uses)
   {
      return new UsesImpl(uses);
   }

   public static Optional<Declared> getDeclared(String qualifiedName)
   {
      try
      {
         Class<?> aClass = Class.forName(qualifiedName);
         return Optional.of(generalize(aClass));
      }
      catch (ClassNotFoundException e)
      {
         return Optional.empty();
      }
   }

   public static Executable generalize(java.lang.reflect.Executable executable)
   {
      return new ExecutableImpl(executable);
   }

   public static Package getPackage(String name)
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

   public static EnumConstant generalize(Enum<?> enumConstant)
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

   public static ModuleDescriptor.Exports particularize(Exports exports)
   {
      return ((ExportsImpl) exports).getReflection();
   }

   public static ModuleDescriptor.Opens particularize(Opens opens)
   {
      return ((OpensImpl) opens).getReflection();
   }

   public static ModuleDescriptor.Provides particularize(Provides provides)
   {
      return ((ProvidesImpl) provides).getReflection();
   }

   public static ModuleDescriptor.Requires particularize(Requires requires)
   {
      return ((RequiresImpl) requires).getReflection();
   }

   public static String particularize(Uses uses)
   {
      return ((UsesImpl) uses).getReflection();
   }

   public static java.lang.annotation.Annotation particularize(AnnotationUsage annotationUsage)
   {
      return ((AnnotationUsageImpl) annotationUsage).getAnnotationReflection();
   }

   public static Class<?> particularize(Array array)
   {
      return ((ArrayImpl) array).getReflection();
   }

   public static Class<?> particularize(Declared declared)
   {
      return ((DeclaredImpl) declared).getReflection();
   }

   public static java.lang.reflect.Field particularize(EnumConstant enumConstant)
   {
      return ((ReflectionFieldImpl<?>) enumConstant).getReflection();
   }

   public static java.lang.reflect.Executable particularize(Executable executable)
   {
      return ((ExecutableImpl) executable).getReflection();
   }

   public static java.lang.reflect.Field particularize(Field field)
   {
      return ((ReflectionFieldImpl<?>) field).getReflection();
   }

   public static TypeVariable<?> particularize(Generic generic)
   {
      return ((GenericImpl) generic).getReflection();
   }

   public static java.lang.reflect.Type[] particularize(Intersection intersection)
   {
      return ((IntersectionImpl) intersection).getReflection();
   }

   public static ModuleDescriptor particularize(Module module)
   {
      return ((ModuleImpl) module).getReflection();
   }

   public static java.lang.Package particularize(Package aPackage)
   {
      return ((PackageImpl) aPackage).getReflection();
   }

   public static java.lang.reflect.Parameter particularize(Parameter parameter)
   {
      return ((ParameterImpl) parameter).getReflection();
   }

   public static java.lang.Class<?> particularize(Primitive primitive)
   {
      return ((PrimitiveImpl) primitive).getReflection();
   }

   public static java.lang.reflect.RecordComponent particularize(RecordComponent recordComponent)
   {
      return ((RecordComponentImpl) recordComponent).getReflection();
   }

   public static WildcardType particularize(Wildcard wildcard)
   {
      return ((WildcardImpl) wildcard).getReflection();
   }
}
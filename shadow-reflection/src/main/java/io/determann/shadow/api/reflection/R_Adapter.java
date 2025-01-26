package io.determann.shadow.api.reflection;

import io.determann.shadow.api.Implementation;
import io.determann.shadow.api.reflection.shadow.R_AnnotationUsage;
import io.determann.shadow.api.reflection.shadow.directive.*;
import io.determann.shadow.api.reflection.shadow.structure.*;
import io.determann.shadow.api.reflection.shadow.type.*;
import io.determann.shadow.api.reflection.shadow.type.primitive.R_Primitive;
import io.determann.shadow.internal.reflection.NamedSupplier;
import io.determann.shadow.internal.reflection.shadow.AnnotationUsageImpl;
import io.determann.shadow.internal.reflection.shadow.directive.*;
import io.determann.shadow.internal.reflection.shadow.structure.*;
import io.determann.shadow.internal.reflection.shadow.type.*;
import io.determann.shadow.internal.reflection.shadow.type.primitive.PrimitiveImpl;

import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface R_Adapter
{
   Implementation IMPLEMENTATION = new Implementation("io.determann.shadow-reflection");

   static <TYPE extends R_Type> TYPE generalize(Class<?> aClass)
   {
      return generalize(aClass, Collections.emptyList());
   }

   @SuppressWarnings("unchecked")
   private static <TYPE extends R_Type> TYPE generalize(Class<?> aClass, List<R_Type> genericTypes)
   {
      if (aClass.isPrimitive())
      {
         if (aClass.equals(Void.TYPE))
         {
            return (TYPE) new VoidImpl();
         }
         return getPrimitive(aClass);
      }
      if (aClass.isArray())
      {
         return (TYPE) new ArrayImpl(aClass);
      }
      if (aClass.isRecord())
      {
         return (TYPE) new RecordImpl(aClass, genericTypes);
      }
      if (aClass.isAnnotation())
      {
         return (TYPE) new AnnotationImpl(aClass);
      }
      if (aClass.isEnum())
      {
         return (TYPE) new EnumImpl(aClass);
      }
      if (aClass.isInterface())
      {
         return (TYPE) new InterfaceImpl(aClass, genericTypes);
      }
      return (TYPE) new ClassImpl(aClass, genericTypes);
   }

   @SuppressWarnings("unchecked")
   private static <TYPE extends R_Type> TYPE getPrimitive(Class<?> aClass)
   {
      if (aClass.equals(Boolean.TYPE))
      {
         return (TYPE) new PrimitiveImpl.R_booleanImpl();
      }
      if (aClass.equals(Byte.TYPE))
      {
         return (TYPE) new PrimitiveImpl.R_byteImpl();
      }
      if (aClass.equals(Short.TYPE))
      {
         return (TYPE) new PrimitiveImpl.R_shortImpl();
      }
      if (aClass.equals(Integer.TYPE))
      {
         return (TYPE) new PrimitiveImpl.R_intImpl();
      }
      if (aClass.equals(Long.TYPE))
      {
         return (TYPE) new PrimitiveImpl.R_longImpl();
      }
      if (aClass.equals(Character.TYPE))
      {
         return (TYPE) new PrimitiveImpl.R_charImpl();
      }
      if (aClass.equals(Float.TYPE))
      {
         return (TYPE) new PrimitiveImpl.R_floatImpl();
      }
      if (aClass.equals(Double.TYPE))
      {
         return (TYPE) new PrimitiveImpl.R_doubleImpl();
      }
      throw new IllegalStateException();
   }

   static R_Package generalize(java.lang.Package aPackage)
   {
      return new PackageImpl(new NamedSupplier<>(aPackage, java.lang.Package::getName));
   }

   static R_Module generalize(java.lang.Module module)
   {
      return new ModuleImpl(new NamedSupplier<>(module.getDescriptor(), ModuleDescriptor::name),
                            Arrays.stream(module.getAnnotations()).map(R_Adapter::generalize).toList());
   }

   static R_Module getModuleType(String name)
   {
      return new ModuleImpl(new NamedSupplier<>(name,
                                                () -> ModuleFinder.ofSystem().find(name).orElseThrow().descriptor(),
                                                ModuleDescriptor::name));
   }

   static R_Module generalize(ModuleReference moduleReference)
   {
      return new ModuleImpl(new NamedSupplier<>(moduleReference.descriptor(), ModuleDescriptor::name));
   }

   static R_Type generalize(java.lang.reflect.Type type)
   {
      if (type instanceof ParameterizedType parameterizedType)
      {
         java.lang.reflect.Type rawType = parameterizedType.getRawType();
         if (rawType instanceof Class<?> aClass)
         {
            return generalize(aClass, Arrays.stream(parameterizedType.getActualTypeArguments()).map(R_Adapter::generalize).toList());
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

   static R_AnnotationUsage generalize(java.lang.annotation.Annotation annotation)
   {
      return new AnnotationUsageImpl(annotation);
   }

   static R_Field generalize(java.lang.reflect.Field field)
   {
      return new FieldImpl(field);
   }

   static R_Method generalize(java.lang.reflect.Method method)
   {
      return new MethodImpl(method);
   }

   static R_Constructor generalize(java.lang.reflect.Constructor<?> constructor)
   {
      return new ConstructorImpl(constructor);
   }

   static R_RecordComponent generalize(java.lang.reflect.RecordComponent recordComponent)
   {
      return new RecordComponentImpl(recordComponent);
   }

   static R_Parameter generalize(java.lang.reflect.Parameter parameter)
   {
      return new ParameterImpl(parameter);
   }

   static R_Type generalize(TypeVariable<?> typeVariable)
   {
      return new GenericImpl(typeVariable);
   }

   static R_Requires generalize(ModuleDescriptor.Requires requires)
   {
      return new RequiresImpl(requires);
   }

   static R_Exports generalize(ModuleDescriptor.Exports exports)
   {
      return new ExportsImpl(exports);
   }

   static R_Opens generalize(ModuleDescriptor.Opens opens)
   {
      return new OpensImpl(opens);
   }

   static R_Provides generalize(ModuleDescriptor.Provides provides)
   {
      return new ProvidesImpl(provides);
   }

   static R_Uses getUsesType(String uses)
   {
      return new UsesImpl(uses);
   }

   static Optional<R_Declared> getDeclared(String qualifiedName)
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

   static R_Executable generalize(java.lang.reflect.Executable executable)
   {
      return switch (executable)
      {
         case Constructor<?> constructor -> new ConstructorImpl(executable);
         case Method method -> new MethodImpl(executable);
      };
   }

   static Optional<R_Package> getPackage(String moduleName, String packageName)
   {
      return getModuleType(moduleName).getPackages().stream().filter(rPackage -> rPackage.getQualifiedName().equals(packageName)).findAny();
   }

   static R_Package getPackage(String name)
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

   static R_EnumConstant generalize(Enum<?> enumConstant)
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

   static ModuleDescriptor.Exports particularize(R_Exports exports)
   {
      return ((ExportsImpl) exports).getReflection();
   }

   static ModuleDescriptor.Opens particularize(R_Opens opens)
   {
      return ((OpensImpl) opens).getReflection();
   }

   static ModuleDescriptor.Provides particularize(R_Provides provides)
   {
      return ((ProvidesImpl) provides).getReflection();
   }

   static ModuleDescriptor.Requires particularize(R_Requires requires)
   {
      return ((RequiresImpl) requires).getReflection();
   }

   static String particularize(R_Uses uses)
   {
      return ((UsesImpl) uses).getReflection();
   }

   static java.lang.annotation.Annotation particularize(R_AnnotationUsage annotationUsage)
   {
      return ((AnnotationUsageImpl) annotationUsage).getAnnotationReflection();
   }

   static Class<?> particularize(R_Array array)
   {
      return ((ArrayImpl) array).getReflection();
   }

   static Class<?> particularize(R_Declared declared)
   {
      return ((DeclaredImpl) declared).getReflection();
   }

   static java.lang.reflect.Field particularize(R_EnumConstant enumConstant)
   {
      return ((ReflectionFieldImpl<?>) enumConstant).getReflection();
   }

   static java.lang.reflect.Executable particularize(R_Executable executable)
   {
      return ((ExecutableImpl) executable).getReflection();
   }

   static java.lang.reflect.Field particularize(R_Field field)
   {
      return ((ReflectionFieldImpl<?>) field).getReflection();
   }

   static TypeVariable<?> particularize(R_Generic generic)
   {
      return ((GenericImpl) generic).getReflection();
   }

   static java.lang.reflect.Type[] particularize(R_Intersection intersection)
   {
      return ((IntersectionImpl) intersection).getReflection();
   }

   static ModuleDescriptor particularize(R_Module module)
   {
      return ((ModuleImpl) module).getReflection();
   }

   static java.lang.Package particularize(R_Package aPackage)
   {
      return ((PackageImpl) aPackage).getReflection();
   }

   static java.lang.reflect.Parameter particularize(R_Parameter parameter)
   {
      return ((ParameterImpl) parameter).getReflection();
   }

   static java.lang.Class<?> particularize(R_Primitive primitive)
   {
      return ((PrimitiveImpl) primitive).getReflection();
   }

   static java.lang.reflect.RecordComponent particularize(R_RecordComponent recordComponent)
   {
      return ((RecordComponentImpl) recordComponent).getReflection();
   }

   static WildcardType particularize(R_Wildcard wildcard)
   {
      return ((WildcardImpl) wildcard).getReflection();
   }
}
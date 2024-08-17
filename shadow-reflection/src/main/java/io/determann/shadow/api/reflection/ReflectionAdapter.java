package io.determann.shadow.api.reflection;

import io.determann.shadow.api.reflection.shadow.AnnotationUsageReflection;
import io.determann.shadow.api.reflection.shadow.directive.*;
import io.determann.shadow.api.reflection.shadow.structure.*;
import io.determann.shadow.api.reflection.shadow.type.*;
import io.determann.shadow.internal.reflection.NamedSupplier;
import io.determann.shadow.internal.reflection.shadow.AnnotationUsageImpl;
import io.determann.shadow.internal.reflection.shadow.directive.*;
import io.determann.shadow.internal.reflection.shadow.structure.*;
import io.determann.shadow.internal.reflection.shadow.type.*;

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
   public static <SHADOW extends ShadowReflection> SHADOW generalize(Class<?> aClass)
   {
      return generalize(aClass, Collections.emptyList());
   }

   @SuppressWarnings("unchecked")
   private static <SHADOW extends ShadowReflection> SHADOW generalize(Class<?> aClass, List<ShadowReflection> genericShadows)
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
      if (aClass.isAnnotation())
      {
         return (SHADOW) new AnnotationImpl(aClass);
      }
      if (aClass.isEnum())
      {
         return (SHADOW) new EnumImpl(aClass);
      }
      if (aClass.isInterface())
      {
         return (SHADOW) new InterfaceImpl(aClass, genericShadows);
      }
      return (SHADOW) new ClassImpl(aClass, genericShadows);
   }

   public static PackageReflection generalize(java.lang.Package aPackage)
   {
      return new PackageImpl(new NamedSupplier<>(aPackage, java.lang.Package::getName));
   }

   public static ModuleReflection generalize(java.lang.Module module)
   {
      return new ModuleImpl(new NamedSupplier<>(module.getDescriptor(), ModuleDescriptor::name),
                            Arrays.stream(module.getAnnotations()).map(ReflectionAdapter::generalize).toList());
   }

   public static ModuleReflection getModuleShadow(String name)
   {
      return new ModuleImpl(new NamedSupplier<>(name,
                                                () -> ModuleFinder.ofSystem().find(name).orElseThrow().descriptor(),
                                                ModuleDescriptor::name));
   }

   public static ModuleReflection generalize(ModuleReference moduleReference)
   {
      return new ModuleImpl(new NamedSupplier<>(moduleReference.descriptor(), ModuleDescriptor::name));
   }

   public static ShadowReflection generalize(java.lang.reflect.Type type)
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

   public static AnnotationUsageReflection generalize(java.lang.annotation.Annotation annotation)
   {
      return new AnnotationUsageImpl(annotation);
   }

   public static FieldReflection generalize(java.lang.reflect.Field field)
   {
      return new FieldImpl(field);
   }

   public static MethodReflection generalize(java.lang.reflect.Method method)
   {
      return new ExecutableImpl(method);
   }

   public static ConstructorReflection generalize(java.lang.reflect.Constructor<?> constructor)
   {
      return new ExecutableImpl(constructor);
   }

   public static RecordComponentReflection generalize(java.lang.reflect.RecordComponent recordComponent)
   {
      return new RecordComponentImpl(recordComponent);
   }

   public static ParameterReflection generalize(java.lang.reflect.Parameter parameter)
   {
      return new ParameterImpl(parameter);
   }

   public static ShadowReflection generalize(TypeVariable<?> typeVariable)
   {
      return new GenericImpl(typeVariable);
   }

   public static RequiresReflection generalize(ModuleDescriptor.Requires requires)
   {
      return new RequiresImpl(requires);
   }

   public static ExportsReflection generalize(ModuleDescriptor.Exports exports)
   {
      return new ExportsImpl(exports);
   }

   public static OpensReflection generalize(ModuleDescriptor.Opens opens)
   {
      return new OpensImpl(opens);
   }

   public static ProvidesReflection generalize(ModuleDescriptor.Provides provides)
   {
      return new ProvidesImpl(provides);
   }

   public static UsesReflection getUsesShadow(String uses)
   {
      return new UsesImpl(uses);
   }

   public static Optional<DeclaredReflection> getDeclared(String qualifiedName)
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

   public static ExecutableReflection generalize(java.lang.reflect.Executable executable)
   {
      return new ExecutableImpl(executable);
   }

   public static PackageReflection getPackage(String name)
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

   public static EnumConstantReflection generalize(Enum<?> enumConstant)
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

   public static ModuleDescriptor.Exports particularize(ExportsReflection exports)
   {
      return ((ExportsImpl) exports).getReflection();
   }

   public static ModuleDescriptor.Opens particularize(OpensReflection opens)
   {
      return ((OpensImpl) opens).getReflection();
   }

   public static ModuleDescriptor.Provides particularize(ProvidesReflection provides)
   {
      return ((ProvidesImpl) provides).getReflection();
   }

   public static ModuleDescriptor.Requires particularize(RequiresReflection requires)
   {
      return ((RequiresImpl) requires).getReflection();
   }

   public static String particularize(UsesReflection uses)
   {
      return ((UsesImpl) uses).getReflection();
   }

   public static java.lang.annotation.Annotation particularize(AnnotationUsageReflection annotationUsage)
   {
      return ((AnnotationUsageImpl) annotationUsage).getAnnotationReflection();
   }

   public static Class<?> particularize(ArrayReflection array)
   {
      return ((ArrayImpl) array).getReflection();
   }

   public static Class<?> particularize(DeclaredReflection declared)
   {
      return ((DeclaredImpl) declared).getReflection();
   }

   public static java.lang.reflect.Field particularize(EnumConstantReflection enumConstant)
   {
      return ((ReflectionFieldImpl<?>) enumConstant).getReflection();
   }

   public static java.lang.reflect.Executable particularize(ExecutableReflection executable)
   {
      return ((ExecutableImpl) executable).getReflection();
   }

   public static java.lang.reflect.Field particularize(FieldReflection field)
   {
      return ((ReflectionFieldImpl<?>) field).getReflection();
   }

   public static TypeVariable<?> particularize(GenericReflection generic)
   {
      return ((GenericImpl) generic).getReflection();
   }

   public static java.lang.reflect.Type[] particularize(IntersectionReflection intersection)
   {
      return ((IntersectionImpl) intersection).getReflection();
   }

   public static ModuleDescriptor particularize(ModuleReflection module)
   {
      return ((ModuleImpl) module).getReflection();
   }

   public static java.lang.Package particularize(PackageReflection aPackage)
   {
      return ((PackageImpl) aPackage).getReflection();
   }

   public static java.lang.reflect.Parameter particularize(ParameterReflection parameter)
   {
      return ((ParameterImpl) parameter).getReflection();
   }

   public static java.lang.Class<?> particularize(PrimitiveReflection primitive)
   {
      return ((PrimitiveImpl) primitive).getReflection();
   }

   public static java.lang.reflect.RecordComponent particularize(RecordComponentReflection recordComponent)
   {
      return ((RecordComponentImpl) recordComponent).getReflection();
   }

   public static WildcardType particularize(WildcardReflection wildcard)
   {
      return ((WildcardImpl) wildcard).getReflection();
   }
}
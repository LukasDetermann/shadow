package io.determann.shadow.internal.reflection.shadow.structure;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.reflection.shadow.AnnotationUsageReflection;
import io.determann.shadow.api.reflection.shadow.structure.*;
import io.determann.shadow.api.reflection.shadow.type.ClassReflection;
import io.determann.shadow.api.reflection.shadow.type.DeclaredReflection;
import io.determann.shadow.api.reflection.shadow.type.GenericReflection;
import io.determann.shadow.api.reflection.shadow.type.ShadowReflection;
import io.determann.shadow.api.shadow.Provider;
import io.determann.shadow.api.shadow.TypeKind;
import io.determann.shadow.api.shadow.modifier.Modifier;
import io.determann.shadow.api.shadow.structure.Executable;
import io.determann.shadow.api.shadow.structure.Method;
import io.determann.shadow.api.shadow.type.Class;
import io.determann.shadow.api.shadow.type.Declared;
import io.determann.shadow.api.shadow.type.Interface;
import io.determann.shadow.internal.reflection.ReflectionUtil;

import java.lang.reflect.AnnotatedType;
import java.util.*;
import java.util.stream.Collectors;

import static io.determann.shadow.api.shadow.Operations.*;
import static io.determann.shadow.api.shadow.Provider.requestOrEmpty;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;
import static io.determann.shadow.api.shadow.modifier.Modifier.*;
import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;

public class ExecutableImpl implements ConstructorReflection,
                                       MethodReflection
{
   private final java.lang.reflect.Executable executable;

   public ExecutableImpl(java.lang.reflect.Executable executable)
   {
      this.executable = executable;
   }

   @Override
   public ModuleReflection getModule()
   {
      return getSurrounding().getModule();
   }

   @Override
   public String getName()
   {
      return getExecutable().getName();
   }

   @Override
   public List<AnnotationUsageReflection> getAnnotationUsages()
   {
      return Arrays.stream(getExecutable().getAnnotations())
                   .map(ReflectionAdapter::generalize)
                   .toList();
   }

   @Override
   public List<AnnotationUsageReflection> getDirectAnnotationUsages()
   {
      return Arrays.stream(getExecutable().getDeclaredAnnotations())
                   .map(ReflectionAdapter::generalize)
                   .toList();
   }

   @Override
   public Set<Modifier> getModifiers()
   {
      boolean isDefault = getExecutable() instanceof java.lang.reflect.Method method && method.isDefault();

      int modifiers = getExecutable().getModifiers() &
                      (getExecutable() instanceof java.lang.reflect.Method
                       ? java.lang.reflect.Modifier.methodModifiers()
                       : java.lang.reflect.Modifier.constructorModifiers());

      boolean isPackagePrivate = !java.lang.reflect.Modifier.isPublic(modifiers) &&
                                 !java.lang.reflect.Modifier.isPrivate(modifiers) &&
                                 !java.lang.reflect.Modifier.isProtected(modifiers);

      return ReflectionUtil.getModifiers(modifiers,
                                         false,
                                         false,
                                         isDefault,
                                         isPackagePrivate);
   }

   @Override
   public List<ParameterReflection> getParameters()
   {
      List<ParameterReflection> result = Arrays.stream(getExecutable().getParameters())
                                     .map(ReflectionAdapter::generalize)
                                     .map(ParameterReflection.class::cast)
                                     .collect(Collectors.toList());

      if (executable instanceof java.lang.reflect.Constructor<?>)
      {
         Optional<ReceiverReflection> receiver = getReceiver();

         if (receiver.isPresent() &&
             !result.isEmpty() &&
             requestOrThrow(result.get(0), VARIABLE_GET_TYPE).equals(requestOrThrow(receiver.get(), RECEIVER_GET_TYPE)))
         {
            result.remove(0);
         }
      }
      return Collections.unmodifiableList(result);
   }

   @Override
   public ReturnReflection getReturn()
   {
      return new ReturnImpl(getExecutable().getAnnotatedReturnType());
   }

   @Override
   public ShadowReflection getReturnType()
   {
      return ReflectionAdapter.generalize(getExecutable().getAnnotatedReturnType().getType());
   }

   @Override
   public List<ShadowReflection> getParameterTypes()
   {
      return Arrays.stream(getExecutable().getParameterTypes()).map(ReflectionAdapter::generalize).map(ShadowReflection.class::cast).toList();
   }

   @Override
   public List<ClassReflection> getThrows()
   {
      return Arrays.stream(getExecutable().getExceptionTypes()).map(ReflectionAdapter::generalize).map(ClassReflection.class::cast).toList();
   }

   @Override
   public boolean isBridge()
   {
      java.lang.reflect.Method method = ((java.lang.reflect.Method) getExecutable());
      return method.isBridge();
   }

   @Override
   public boolean isVarArgs()
   {
      return getExecutable().isVarArgs();
   }

   @Override
   public DeclaredReflection getSurrounding()
   {
      return ReflectionAdapter.generalize(getExecutable().getDeclaringClass());
   }

   @Override
   public PackageReflection getPackage()
   {
      return getSurrounding().getPackage();
   }

   @Override
   public List<GenericReflection> getGenerics()
   {
      return Arrays.stream(getExecutable().getTypeParameters()).map(ReflectionAdapter::generalize).map(GenericReflection.class::cast).toList();
   }

   @Override
   public Optional<DeclaredReflection> getReceiverType()
   {
      AnnotatedType receiverType = getExecutable().getAnnotatedReceiverType();
      if (receiverType == null)
      {
         return Optional.empty();
      }
      //there is no way to tell via reflection, if a ReceiverType is present. getAnnotatedReceiverType() just builds a new one if
      //one could be there. pretending there is no ReceiverType if it is not annotated should be right most of the time.
      if (receiverType.getAnnotations().length == 0)
      {
         return Optional.empty();
      }
      return Optional.of(((DeclaredReflection) ReflectionAdapter.generalize(receiverType.getType())));
   }

   @Override
   public Optional<ReceiverReflection> getReceiver()
   {
      AnnotatedType receiverType = getExecutable().getAnnotatedReceiverType();
      if (receiverType == null)
      {
         return Optional.empty();
      }
      //there is no way to tell via reflection, if a ReceiverType is present. getAnnotatedReceiverType() just builds a new one if
      //one could be there. pretending there is no ReceiverType if it is not annotated should be right most of the time.
      if (receiverType.getAnnotations().length == 0)
      {
         return Optional.empty();
      }
      return Optional.of((new ReceiverImpl(receiverType)));
   }

   @Override
   public boolean overrides(Method method)
   {
      if (!isSubSignature(method))
      {
         return false;
      }

      if (isStatic() || requestOrThrow(method, MODIFIABLE_HAS_MODIFIER, STATIC))
      {
         return false;
      }

      Declared otherSurrounding = requestOrThrow(method, EXECUTABLE_GET_SURROUNDING);

      if (TypeKind.CLASS.equals(requestOrThrow(otherSurrounding, SHADOW_GET_KIND)))
      {
         if (!requestOrThrow(method, MODIFIABLE_HAS_MODIFIER, PUBLIC) &&
             !requestOrThrow(method, MODIFIABLE_HAS_MODIFIER, PROTECTED) &&
             (!requestOrThrow(method, MODIFIABLE_HAS_MODIFIER, PACKAGE_PRIVATE) || !requestOrThrow(method, EXECUTABLE_GET_PACKAGE).equals(getPackage())))
         {
            return false;
         }

         if (!TypeKind.CLASS.equals(requestOrThrow(getSurrounding(), SHADOW_GET_KIND)))
         {
            return false;
         }
         Class otherSurroundingClass = ((Class) otherSurrounding);
         Class surroundingClass = ((Class) getSurrounding());
         if (!requestOrThrow(surroundingClass, DECLARED_IS_SUBTYPE_OF, otherSurroundingClass))
         {
            return false;
         }
      }
      if (TypeKind.INTERFACE.equals(requestOrThrow(otherSurrounding, SHADOW_GET_KIND)))
      {
         if (!requestOrThrow(method, MODIFIABLE_HAS_MODIFIER, PUBLIC))
         {
            return false;
         }

         if (!TypeKind.CLASS.equals(requestOrThrow(getSurrounding(), SHADOW_GET_KIND)))
         {
            return false;
         }
         Interface otherSurroundingInterface = ((Interface) otherSurrounding);
         Class surroundingClass = ((Class) getSurrounding());
         if (!requestOrThrow(surroundingClass, DECLARED_GET_INTERFACES).contains(otherSurroundingInterface))
         {
            return false;
         }
      }
      return true;
   }

   private boolean isSubSignature(Executable executable)
   {
      return Provider.requestOrEmpty(executable, NAMEABLE_GET_NAME).map(name -> Objects.equals(getName(), name)).orElse(false) &&
             (getParameterTypes().equals(requestOrThrow(executable, EXECUTABLE_GET_PARAMETER_TYPES)));
   }

   @Override
   public boolean overwrittenBy(Method method)
   {
      return requestOrThrow(method, METHOD_OVERRIDES, this);
   }

   @Override
   public boolean sameParameterTypes(Method method)
   {
      return getParameterTypes().equals(requestOrThrow(method, EXECUTABLE_GET_PARAMETER_TYPES));
   }

   public java.lang.reflect.Executable getExecutable()
   {
      return executable;
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getName(),
                          getParameterTypes(),
                          getParameters(),
                          getModifiers());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof Executable otherExecutable))
      {
         return false;
      }
      return Provider.requestOrEmpty(otherExecutable, NAMEABLE_GET_NAME).map(name -> Objects.equals(getName(), name)).orElse(false) &&
             Objects.equals(getParameters(), requestOrThrow(otherExecutable, EXECUTABLE_GET_PARAMETERS)) &&
             requestOrEmpty(otherExecutable, MODIFIABLE_GET_MODIFIERS).map(modifiers -> Objects.equals(modifiers, getModifiers())).orElse(false) &&
             Objects.equals(getParameterTypes(), requestOrThrow(otherExecutable, EXECUTABLE_GET_PARAMETER_TYPES));
   }

   public java.lang.reflect.Executable getReflection()
   {
      return getExecutable();
   }

   @Override
   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
   }
}

package io.determann.shadow.internal.reflection.shadow.structure;

import io.determann.shadow.api.Provider;
import io.determann.shadow.api.reflection.R_Adapter;
import io.determann.shadow.api.reflection.shadow.R_AnnotationUsage;
import io.determann.shadow.api.reflection.shadow.structure.*;
import io.determann.shadow.api.reflection.shadow.type.R_Class;
import io.determann.shadow.api.reflection.shadow.type.R_Declared;
import io.determann.shadow.api.reflection.shadow.type.R_Generic;
import io.determann.shadow.api.reflection.shadow.type.R_Shadow;
import io.determann.shadow.api.shadow.C_TypeKind;
import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.structure.C_Executable;
import io.determann.shadow.api.shadow.structure.C_Method;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Declared;
import io.determann.shadow.api.shadow.type.C_Interface;
import io.determann.shadow.internal.reflection.ReflectionUtil;

import java.lang.reflect.AnnotatedType;
import java.util.*;
import java.util.stream.Collectors;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.shadow.modifier.C_Modifier.*;
import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;

public class ExecutableImpl implements R_Constructor,
                                       R_Method
{
   private final java.lang.reflect.Executable executable;

   public ExecutableImpl(java.lang.reflect.Executable executable)
   {
      this.executable = executable;
   }

   @Override
   public R_Module getModule()
   {
      return getSurrounding().getModule();
   }

   @Override
   public String getName()
   {
      return getExecutable().getName();
   }

   @Override
   public List<R_AnnotationUsage> getAnnotationUsages()
   {
      return Arrays.stream(getExecutable().getAnnotations())
                   .map(R_Adapter::generalize)
                   .toList();
   }

   @Override
   public List<R_AnnotationUsage> getDirectAnnotationUsages()
   {
      return Arrays.stream(getExecutable().getDeclaredAnnotations())
                   .map(R_Adapter::generalize)
                   .toList();
   }

   @Override
   public Set<C_Modifier> getModifiers()
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
   public List<R_Parameter> getParameters()
   {
      List<R_Parameter> result = Arrays.stream(getExecutable().getParameters())
                                       .map(R_Adapter::generalize)
                                       .map(R_Parameter.class::cast)
                                       .collect(Collectors.toList());

      if (executable instanceof java.lang.reflect.Constructor<?>)
      {
         Optional<R_Receiver> receiver = getReceiver();

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
   public R_Return getReturn()
   {
      return new ReturnImpl(getExecutable().getAnnotatedReturnType());
   }

   @Override
   public R_Shadow getReturnType()
   {
      return R_Adapter.generalize(getExecutable().getAnnotatedReturnType().getType());
   }

   @Override
   public List<R_Shadow> getParameterTypes()
   {
      return Arrays.stream(getExecutable().getParameterTypes()).map(R_Adapter::generalize).map(R_Shadow.class::cast).toList();
   }

   @Override
   public List<R_Class> getThrows()
   {
      return Arrays.stream(getExecutable().getExceptionTypes()).map(R_Adapter::generalize).map(R_Class.class::cast).toList();
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
   public R_Declared getSurrounding()
   {
      return R_Adapter.generalize(getExecutable().getDeclaringClass());
   }

   @Override
   public R_Package getPackage()
   {
      return getSurrounding().getPackage();
   }

   @Override
   public List<R_Generic> getGenerics()
   {
      return Arrays.stream(getExecutable().getTypeParameters()).map(R_Adapter::generalize).map(R_Generic.class::cast).toList();
   }

   @Override
   public Optional<R_Declared> getReceiverType()
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
      return Optional.of(((R_Declared) R_Adapter.generalize(receiverType.getType())));
   }

   @Override
   public Optional<R_Receiver> getReceiver()
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
   public boolean overrides(C_Method method)
   {
      if (!isSubSignature(method))
      {
         return false;
      }

      if (isStatic() || requestOrThrow(method, MODIFIABLE_HAS_MODIFIER, STATIC))
      {
         return false;
      }

      C_Declared otherSurrounding = requestOrThrow(method, EXECUTABLE_GET_SURROUNDING);

      if (C_TypeKind.CLASS.equals(requestOrThrow(otherSurrounding, SHADOW_GET_KIND)))
      {
         if (!requestOrThrow(method, MODIFIABLE_HAS_MODIFIER, PUBLIC) &&
             !requestOrThrow(method, MODIFIABLE_HAS_MODIFIER, PROTECTED) &&
             (!requestOrThrow(method, MODIFIABLE_HAS_MODIFIER, PACKAGE_PRIVATE) || !requestOrThrow(method, EXECUTABLE_GET_PACKAGE).equals(getPackage())))
         {
            return false;
         }

         if (!C_TypeKind.CLASS.equals(requestOrThrow(getSurrounding(), SHADOW_GET_KIND)))
         {
            return false;
         }
         C_Class otherSurroundingClass = ((C_Class) otherSurrounding);
         C_Class surroundingClass = ((C_Class) getSurrounding());
         if (!requestOrThrow(surroundingClass, DECLARED_IS_SUBTYPE_OF, otherSurroundingClass))
         {
            return false;
         }
      }
      if (C_TypeKind.INTERFACE.equals(requestOrThrow(otherSurrounding, SHADOW_GET_KIND)))
      {
         if (!requestOrThrow(method, MODIFIABLE_HAS_MODIFIER, PUBLIC))
         {
            return false;
         }

         if (!C_TypeKind.CLASS.equals(requestOrThrow(getSurrounding(), SHADOW_GET_KIND)))
         {
            return false;
         }
         C_Interface otherSurroundingInterface = ((C_Interface) otherSurrounding);
         C_Class surroundingClass = ((C_Class) getSurrounding());
         if (!requestOrThrow(surroundingClass, DECLARED_GET_INTERFACES).contains(otherSurroundingInterface))
         {
            return false;
         }
      }
      return true;
   }

   private boolean isSubSignature(C_Executable executable)
   {
      return Provider.requestOrEmpty(executable, NAMEABLE_GET_NAME).map(name -> Objects.equals(getName(), name)).orElse(false) &&
             (getParameterTypes().equals(requestOrThrow(executable, EXECUTABLE_GET_PARAMETER_TYPES)));
   }

   @Override
   public boolean overwrittenBy(C_Method method)
   {
      return requestOrThrow(method, METHOD_OVERRIDES, this);
   }

   @Override
   public boolean sameParameterTypes(C_Method method)
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
      if (!(other instanceof C_Executable otherExecutable))
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

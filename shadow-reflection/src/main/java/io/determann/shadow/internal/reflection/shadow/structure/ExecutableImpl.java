package io.determann.shadow.internal.reflection.shadow.structure;

import io.determann.shadow.api.Implementation;
import io.determann.shadow.api.reflection.R_Adapter;
import io.determann.shadow.api.reflection.shadow.R_AnnotationUsage;
import io.determann.shadow.api.reflection.shadow.modifier.R_StaticModifiable;
import io.determann.shadow.api.reflection.shadow.structure.R_Module;
import io.determann.shadow.api.reflection.shadow.structure.R_Parameter;
import io.determann.shadow.api.reflection.shadow.structure.R_Receiver;
import io.determann.shadow.api.reflection.shadow.structure.R_Result;
import io.determann.shadow.api.reflection.shadow.type.R_Class;
import io.determann.shadow.api.reflection.shadow.type.R_Declared;
import io.determann.shadow.api.reflection.shadow.type.R_Generic;
import io.determann.shadow.api.reflection.shadow.type.R_Type;
import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.structure.C_Executable;
import io.determann.shadow.api.shadow.structure.C_Method;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Declared;
import io.determann.shadow.api.shadow.type.C_Interface;
import io.determann.shadow.internal.reflection.ReflectionUtil;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.reflection.R_Adapter.IMPLEMENTATION;
import static io.determann.shadow.api.shadow.modifier.C_Modifier.*;

public abstract class ExecutableImpl implements R_StaticModifiable
{
   private final Executable executable;

   public ExecutableImpl(Executable executable)
   {
      this.executable = executable;
   }

   public R_Module getModule()
   {
      return getSurrounding().getModule();
   }

   public String getName()
   {
      return getExecutable().getName();
   }

   public List<R_AnnotationUsage> getAnnotationUsages()
   {
      return Arrays.stream(getExecutable().getAnnotations())
                   .map(R_Adapter::generalize)
                   .toList();
   }

   public List<R_AnnotationUsage> getDirectAnnotationUsages()
   {
      return Arrays.stream(getExecutable().getDeclaredAnnotations())
                   .map(R_Adapter::generalize)
                   .toList();
   }

   @Override
   public Set<C_Modifier> getModifiers()
   {
      boolean isDefault = getExecutable() instanceof Method method && method.isDefault();

      int modifiers = getExecutable().getModifiers() &
                      (getExecutable() instanceof Method
                       ? Modifier.methodModifiers()
                       : Modifier.constructorModifiers());

      boolean isPackagePrivate = !Modifier.isPublic(modifiers) &&
                                 !Modifier.isPrivate(modifiers) &&
                                 !Modifier.isProtected(modifiers);

      return ReflectionUtil.getModifiers(modifiers,
                                         false,
                                         false,
                                         isDefault,
                                         isPackagePrivate);
   }

   public List<R_Parameter> getParameters()
   {
      List<R_Parameter> result = Arrays.stream(getExecutable().getParameters())
                                       .map(R_Adapter::generalize)
                                       .map(R_Parameter.class::cast)
                                       .collect(Collectors.toList());

      if (executable instanceof Constructor<?>)
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

   public R_Result getReturn()
   {
      return new ResultImpl(getExecutable().getAnnotatedReturnType());
   }

   public R_Type getReturnType()
   {
      return R_Adapter.generalize(getExecutable().getAnnotatedReturnType().getType());
   }

   public List<R_Type> getParameterTypes()
   {
      return Arrays.stream(getExecutable().getParameterTypes()).map(R_Adapter::generalize).map(R_Type.class::cast).toList();
   }

   public List<R_Class> getThrows()
   {
      return Arrays.stream(getExecutable().getExceptionTypes()).map(R_Adapter::generalize).map(R_Class.class::cast).toList();
   }

   public boolean isBridge()
   {
      Method method = ((Method) getExecutable());
      return method.isBridge();
   }

   public boolean isVarArgs()
   {
      return getExecutable().isVarArgs();
   }

   public R_Declared getSurrounding()
   {
      return R_Adapter.generalize(getExecutable().getDeclaringClass());
   }

   public List<R_Generic> getGenerics()
   {
      return Arrays.stream(getExecutable().getTypeParameters()).map(R_Adapter::generalize).map(R_Generic.class::cast).toList();
   }

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

      if (otherSurrounding instanceof C_Class otherSurroundingClass)
      {
         if (!requestOrThrow(method, MODIFIABLE_HAS_MODIFIER, PUBLIC) &&
             !requestOrThrow(method, MODIFIABLE_HAS_MODIFIER, PROTECTED) &&
             (!requestOrThrow(method, MODIFIABLE_HAS_MODIFIER, PACKAGE_PRIVATE) ||
              !requestOrThrow(otherSurroundingClass, DECLARED_GET_PACKAGE).equals(getSurrounding().getPackage())))
         {
            return false;
         }

         if (!(getSurrounding() instanceof C_Class surroundingClass))
         {
            return false;
         }
         if (!requestOrThrow(surroundingClass, DECLARED_IS_SUBTYPE_OF, otherSurroundingClass))
         {
            return false;
         }
      }
      if (otherSurrounding instanceof C_Interface otherSurroundingInterface)
      {
         if (!requestOrThrow(method, MODIFIABLE_HAS_MODIFIER, PUBLIC))
         {
            return false;
         }

         if (!(getSurrounding() instanceof C_Class surroundingClass))
         {
            return false;
         }
         if (!requestOrThrow(surroundingClass, DECLARED_GET_INTERFACES).contains(otherSurroundingInterface))
         {
            return false;
         }
      }
      return true;
   }

   private boolean isSubSignature(C_Executable executable)
   {
      return requestOrEmpty(executable, NAMEABLE_GET_NAME).map(name -> Objects.equals(getName(), name)).orElse(false) &&
             (getParameterTypes().equals(requestOrThrow(executable, EXECUTABLE_GET_PARAMETER_TYPES)));
   }

   public boolean overwrittenBy(C_Method method)
   {
      return requestOrThrow(method, METHOD_OVERRIDES, ((C_Method) this));
   }

   public boolean sameParameterTypes(C_Method method)
   {
      return getParameterTypes().equals(requestOrThrow(method, EXECUTABLE_GET_PARAMETER_TYPES));
   }

   public Executable getExecutable()
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
      return requestOrEmpty(otherExecutable, NAMEABLE_GET_NAME).map(name -> Objects.equals(getName(), name)).orElse(false) &&
             Objects.equals(getParameters(), requestOrThrow(otherExecutable, EXECUTABLE_GET_PARAMETERS)) &&
             requestOrEmpty(otherExecutable, MODIFIABLE_GET_MODIFIERS).map(modifiers -> Objects.equals(modifiers, getModifiers()))
                                                                      .orElse(false) &&
             Objects.equals(getParameterTypes(), requestOrThrow(otherExecutable, EXECUTABLE_GET_PARAMETER_TYPES));
   }

   public Executable getReflection()
   {
      return getExecutable();
   }

   public Implementation getImplementation()
   {
      return IMPLEMENTATION;
   }
}

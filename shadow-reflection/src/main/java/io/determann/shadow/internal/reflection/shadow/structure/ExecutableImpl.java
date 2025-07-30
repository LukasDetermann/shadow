package io.determann.shadow.internal.reflection.shadow.structure;

import io.determann.shadow.api.C;
import io.determann.shadow.api.Modifier;
import io.determann.shadow.api.query.Implementation;
import io.determann.shadow.api.reflection.Adapter;
import io.determann.shadow.api.reflection.R;
import io.determann.shadow.internal.reflection.ReflectionUtil;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static io.determann.shadow.api.Modifier.*;
import static io.determann.shadow.api.query.Operations.*;
import static io.determann.shadow.api.query.Provider.requestOrEmpty;
import static io.determann.shadow.api.query.Provider.requestOrThrow;
import static io.determann.shadow.api.reflection.Adapter.IMPLEMENTATION;

public abstract class ExecutableImpl implements R.StaticModifiable
{
   private final Executable executable;

   public ExecutableImpl(Executable executable)
   {
      this.executable = executable;
   }

   public R.Module getModule()
   {
      return getSurrounding().getModule();
   }

   public String getName()
   {
      return getExecutable().getName();
   }

   public List<R.AnnotationUsage> getAnnotationUsages()
   {
      return Arrays.stream(getExecutable().getAnnotations())
                   .map(Adapter::generalize)
                   .toList();
   }

   public List<R.AnnotationUsage> getDirectAnnotationUsages()
   {
      return Arrays.stream(getExecutable().getDeclaredAnnotations())
                   .map(Adapter::generalize)
                   .toList();
   }

   @Override
   public Set<Modifier> getModifiers()
   {
      boolean isDefault = getExecutable() instanceof Method method && method.isDefault();

      int modifiers = getExecutable().getModifiers() &
                      (getExecutable() instanceof Method
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

   public List<R.Parameter> getParameters()
   {
      List<R.Parameter> result = Arrays.stream(getExecutable().getParameters())
                                       .map(Adapter::generalize)
                                       .map(R.Parameter.class::cast)
                                       .collect(Collectors.toList());

      if (executable instanceof Constructor<?>)
      {
         Optional<R.Receiver> receiver = getReceiver();

         if (receiver.isPresent() &&
             !result.isEmpty() &&
             requestOrThrow(result.get(0), VARIABLE_GET_TYPE).equals(requestOrThrow(receiver.get(), RECEIVER_GET_TYPE)))
         {
            result.remove(0);
         }
      }
      return Collections.unmodifiableList(result);
   }

   public R.Result getResult()
   {
      return new ResultImpl(getExecutable().getAnnotatedReturnType());
   }

   public R.Type getReturnType()
   {
      return Adapter.generalize(getExecutable().getAnnotatedReturnType().getType());
   }

   public List<R.Type> getParameterTypes()
   {
      return Arrays.stream(getExecutable().getParameterTypes()).map(Adapter::generalize).map(R.Type.class::cast).toList();
   }

   public List<R.Class> getThrows()
   {
      return Arrays.stream(getExecutable().getExceptionTypes()).map(Adapter::generalize).map(R.Class.class::cast).toList();
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

   public R.Declared getSurrounding()
   {
      return Adapter.generalize(getExecutable().getDeclaringClass());
   }

   public List<R.Generic> getGenerics()
   {
      return Arrays.stream(getExecutable().getTypeParameters()).map(Adapter::generalize).map(R.Generic.class::cast).toList();
   }

   public Optional<R.Declared> getReceiverType()
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
      return Optional.of(((R.Declared) Adapter.generalize(receiverType.getType())));
   }

   public Optional<R.Receiver> getReceiver()
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

   public boolean overrides(C.Method method)
   {
      if (!isSubSignature(method))
      {
         return false;
      }

      if (isStatic() || requestOrThrow(method, MODIFIABLE_HAS_MODIFIER, STATIC))
      {
         return false;
      }

      C.Declared otherSurrounding = requestOrThrow(method, EXECUTABLE_GET_SURROUNDING);

      if (otherSurrounding instanceof C.Class otherSurroundingClass)
      {
         if (!requestOrThrow(method, MODIFIABLE_HAS_MODIFIER, PUBLIC) &&
             !requestOrThrow(method, MODIFIABLE_HAS_MODIFIER, PROTECTED) &&
             (!requestOrThrow(method, MODIFIABLE_HAS_MODIFIER, PACKAGE_PRIVATE) ||
              !requestOrThrow(otherSurroundingClass, DECLARED_GET_PACKAGE).equals(getSurrounding().getPackage())))
         {
            return false;
         }

         if (!(getSurrounding() instanceof C.Class surroundingClass))
         {
            return false;
         }
         if (!requestOrThrow(surroundingClass, DECLARED_IS_SUBTYPE_OF, otherSurroundingClass))
         {
            return false;
         }
      }
      if (otherSurrounding instanceof C.Interface otherSurroundingInterface)
      {
         if (!requestOrThrow(method, MODIFIABLE_HAS_MODIFIER, PUBLIC))
         {
            return false;
         }

         if (!(getSurrounding() instanceof C.Class surroundingClass))
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

   private boolean isSubSignature(C.Executable executable)
   {
      return requestOrEmpty(executable, NAMEABLE_GET_NAME).map(name -> Objects.equals(getName(), name)).orElse(false) &&
             (getParameterTypes().equals(requestOrThrow(executable, EXECUTABLE_GET_PARAMETER_TYPES)));
   }

   public boolean overwrittenBy(C.Method method)
   {
      return requestOrThrow(method, METHOD_OVERRIDES, ((C.Method) this));
   }

   public boolean sameParameterTypes(C.Method method)
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
      if (!(other instanceof C.Executable otherExecutable))
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

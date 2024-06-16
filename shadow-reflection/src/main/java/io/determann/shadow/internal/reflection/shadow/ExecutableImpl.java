package io.determann.shadow.internal.reflection.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.modifier.Modifier;
import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.reflection.query.ConstructorReflection;
import io.determann.shadow.api.reflection.query.MethodReflection;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.*;
import io.determann.shadow.internal.reflection.ReflectionUtil;

import java.lang.reflect.AnnotatedType;
import java.util.*;
import java.util.stream.Collectors;

import static io.determann.shadow.api.converter.Converter.convert;
import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;
import static io.determann.shadow.meta_meta.Operations.*;
import static io.determann.shadow.meta_meta.Provider.request;
import static io.determann.shadow.meta_meta.Provider.requestOrThrow;

public class ExecutableImpl implements ConstructorReflection,
                                       MethodReflection
{
   private final java.lang.reflect.Executable executable;

   public ExecutableImpl(java.lang.reflect.Executable executable)
   {
      this.executable = executable;
   }

   @Override
   public Module getModule()
   {
      return requestOrThrow(getSurrounding(), MODULE_ENCLOSED_GET_MODULE);
   }

   @Override
   public String getName()
   {
      return getExecutable().getName();
   }

   @Override
   public List<AnnotationUsage> getAnnotationUsages()
   {
      return Arrays.stream(getExecutable().getAnnotations())
                   .map(ReflectionAdapter::generalize)
                   .toList();
   }

   @Override
   public List<AnnotationUsage> getDirectAnnotationUsages()
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
      return ReflectionUtil.getModifiers(modifiers,
                                         false,
                                         isDefault);
   }

   @Override
   public List<Parameter> getParameters()
   {
      List<Parameter> result = Arrays.stream(getExecutable().getParameters())
                                     .map(ReflectionAdapter::generalize)
                                     .map(Parameter.class::cast)
                                     .collect(Collectors.toList());

      if (executable instanceof java.lang.reflect.Constructor<?>)
      {
         Optional<Receiver> receiver = getReceiver();

         if (receiver.isPresent() && !result.isEmpty() && requestOrThrow(result.get(0), VARIABLE_GET_TYPE).equals(receiver.get().getType()))
         {
            result.remove(0);
         }
      }
      return Collections.unmodifiableList(result);
   }

   @Override
   public Return getReturn()
   {
      return new ReturnImpl(getExecutable().getAnnotatedReturnType());
   }

   @Override
   public Shadow getReturnType()
   {
      return ReflectionAdapter.generalize(getExecutable().getAnnotatedReturnType().getType());
   }

   @Override
   public List<Shadow> getParameterTypes()
   {
      return Arrays.stream(getExecutable().getParameterTypes()).map(ReflectionAdapter::generalize).map(Shadow.class::cast).toList();
   }

   @Override
   public List<Class> getThrows()
   {
      return Arrays.stream(getExecutable().getExceptionTypes()).map(ReflectionAdapter::generalize).map(Class.class::cast).toList();
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
   public Declared getSurrounding()
   {
      return ReflectionAdapter.generalize(getExecutable().getDeclaringClass());
   }

   @Override
   public Package getPackage()
   {
      return requestOrThrow(getSurrounding(), DECLARED_GET_PACKAGE);
   }

   @Override
   public List<Generic> getGenerics()
   {
      return Arrays.stream(getExecutable().getTypeParameters()).map(ReflectionAdapter::generalize).map(Generic.class::cast).toList();
   }

   @Override
   public Optional<Declared> getReceiverType()
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
      return Optional.of(((Declared) ReflectionAdapter.generalize(receiverType.getType())));
   }

   @Override
   public Optional<Receiver> getReceiver()
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

      if (isStatic() || method.isStatic())
      {
         return false;
      }

      Declared otherSurrounding = requestOrThrow(method, EXECUTABLE_GET_SURROUNDING);

      if (TypeKind.CLASS.equals(requestOrThrow(otherSurrounding, SHADOW_GET_KIND)))
      {
         if (!method.isPublic() && !method.isProtected() && (!method.isPackagePrivate() || !requestOrThrow(method, EXECUTABLE_GET_PACKAGE).equals(getPackage())))
         {
            return false;
         }

         if (!TypeKind.CLASS.equals(requestOrThrow(getSurrounding(), SHADOW_GET_KIND)))
         {
            return false;
         }
         Class otherSurroundingClass = convert(otherSurrounding).toClassOrThrow();
         Class surroundingClass = convert(getSurrounding()).toClassOrThrow();
         if (!requestOrThrow(surroundingClass, DECLARED_IS_SUBTYPE_OF, otherSurroundingClass))
         {
            return false;
         }
      }
      if (TypeKind.INTERFACE.equals(requestOrThrow(otherSurrounding, SHADOW_GET_KIND)))
      {
         if (!method.isPublic())
         {
            return false;
         }

         if (!TypeKind.CLASS.equals(requestOrThrow(getSurrounding(), SHADOW_GET_KIND)))
         {
            return false;
         }
         Interface otherSurroundingInterface = convert(otherSurrounding).toInterfaceOrThrow();
         Class surroundingClass = convert(getSurrounding()).toClassOrThrow();
         if (!requestOrThrow(surroundingClass, DECLARED_GET_INTERFACES).contains(otherSurroundingInterface))
         {
            return false;
         }
      }
      return true;
   }

   private boolean isSubSignature(Executable executable)
   {
      return request(executable, NAMEABLE_NAME).map(name -> Objects.equals(getName(), name)).orElse(false) &&
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
      return request(otherExecutable, NAMEABLE_NAME).map(name -> Objects.equals(getName(), name)).orElse(false) &&
             Objects.equals(getParameters(), requestOrThrow(otherExecutable, EXECUTABLE_GET_PARAMETERS)) &&
             Objects.equals(getModifiers(), otherExecutable.getModifiers()) &&
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

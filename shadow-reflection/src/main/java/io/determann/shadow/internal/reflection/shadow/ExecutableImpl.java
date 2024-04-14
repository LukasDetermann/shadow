package io.determann.shadow.internal.reflection.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.modifier.Modifier;
import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.*;
import io.determann.shadow.internal.reflection.ReflectionUtil;

import java.lang.reflect.AnnotatedType;
import java.util.*;
import java.util.stream.Collectors;

import static io.determann.shadow.api.converter.Converter.convert;

public class ExecutableImpl implements Constructor,
                                       Method
{
   private final java.lang.reflect.Executable executable;

   public ExecutableImpl(java.lang.reflect.Executable executable)
   {
      this.executable = executable;
   }

   @Override
   public Module getModule()
   {
      return getSurrounding().getModule();
   }

   @Override
   public String getName()
   {
      return getExecutable().getName();
   }

   @Override
   public String getJavaDoc()
   {
      return null;
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

         if (receiver.isPresent() && !result.isEmpty() && result.get(0).getType().equals(receiver.get().getType()))
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
      return getSurrounding().getPackage();
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

      Declared otherSurrounding = method.getSurrounding();

      if (otherSurrounding.isKind(TypeKind.CLASS))
      {
         if (!method.isPublic() && !method.isProtected() && (!method.isPackagePrivate() || !method.getPackage().equals(getPackage())))
         {
            return false;
         }

         if (!getSurrounding().isKind(TypeKind.CLASS))
         {
            return false;
         }
         Class otherSurroundingClass = convert(otherSurrounding).toClassOrThrow();
         Class surroundingClass = convert(getSurrounding()).toClassOrThrow();
         if (!surroundingClass.isSubtypeOf(otherSurroundingClass))
         {
            return false;
         }
      }
      if (otherSurrounding.isKind(TypeKind.INTERFACE))
      {
         if (!method.isPublic())
         {
            return false;
         }

         if (!getSurrounding().isKind(TypeKind.CLASS))
         {
            return false;
         }
         Interface otherSurroundingInterface = convert(otherSurrounding).toInterfaceOrThrow();
         Class surroundingClass = convert(getSurrounding()).toClassOrThrow();
         if (!surroundingClass.getInterfaces().contains(otherSurroundingInterface))
         {
            return false;
         }
      }
      return true;
   }

   private boolean isSubSignature(Executable executable)
   {
      return getName().equals(executable.getName()) && (getParameterTypes().equals(executable.getParameterTypes()));
   }

   @Override
   public boolean overwrittenBy(Method method)
   {
      return method.overwrittenBy(this);
   }

   @Override
   public boolean sameParameterTypes(Method method)
   {
      return getParameterTypes().equals(method.getParameterTypes());
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
      return Objects.equals(getName(), otherExecutable.getName()) &&
             Objects.equals(getParameters(), otherExecutable.getParameters()) &&
             Objects.equals(getModifiers(), otherExecutable.getModifiers()) &&
             Objects.equals(getParameterTypes(), otherExecutable.getParameterTypes());
   }

   public java.lang.reflect.Executable getReflection()
   {
      return getExecutable();
   }
}

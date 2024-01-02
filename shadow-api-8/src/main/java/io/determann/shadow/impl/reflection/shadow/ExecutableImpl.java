package io.determann.shadow.impl.reflection.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.converter.Converter;
import io.determann.shadow.api.modifier.Modifier;
import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.*;
import io.determann.shadow.impl.reflection.ReflectionUtil;

import java.lang.reflect.AnnotatedType;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public class ExecutableImpl implements Constructor,
                                       Method
{
   private final java.lang.reflect.Executable executable;

   public ExecutableImpl(java.lang.reflect.Executable executable)
   {
      this.executable = executable;
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
                   .map(ReflectionAdapter::getAnnotationUsage)
                   .collect(collectingAndThen(toList(), Collections::unmodifiableList));
   }

   @Override
   public List<AnnotationUsage> getDirectAnnotationUsages()
   {
      return Arrays.stream(getExecutable().getDeclaredAnnotations())
                   .map(ReflectionAdapter::getAnnotationUsage)
                   .collect(collectingAndThen(toList(), Collections::unmodifiableList));
   }

   @Override
   public Set<Modifier> getModifiers()
   {
      boolean isDefault = false;
      if (getExecutable() instanceof java.lang.reflect.Method)
      {
         java.lang.reflect.Method method = ((java.lang.reflect.Method) getExecutable());
         isDefault = method.isDefault();
      }
      int modifiers = getExecutable().getModifiers() &
                      (getExecutable() instanceof java.lang.reflect.Method
                       ? java.lang.reflect.Modifier.methodModifiers()
                       : java.lang.reflect.Modifier.constructorModifiers());
      return ReflectionUtil.getModifiers(modifiers, isDefault);
   }

   @Override
   public List<Parameter> getParameters()
   {
      List<Parameter> result = Arrays.stream(getExecutable().getParameters())
                                     .map(ReflectionAdapter::getShadow)
                                     .map(Parameter.class::cast)
                                     .collect(Collectors.toList());

      if (isTypeKind(TypeKind.CONSTRUCTOR))
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
      return ReflectionAdapter.getShadow(getExecutable().getAnnotatedReturnType().getType());
   }

   @Override
   public List<Shadow> getParameterTypes()
   {
      return Arrays.stream(getExecutable().getParameterTypes())
                   .map(ReflectionAdapter::getShadow)
                   .map(Shadow.class::cast)
                   .collect(collectingAndThen(toList(), Collections::unmodifiableList));
   }

   @Override
   public List<Class> getThrows()
   {
      return Arrays.stream(getExecutable().getExceptionTypes())
                   .map(ReflectionAdapter::getShadow)
                   .map(Class.class::cast)
                   .collect(collectingAndThen(toList(), Collections::unmodifiableList));
   }

   @Override
   public boolean isVarArgs()
   {
      return getExecutable().isVarArgs();
   }

   @Override
   public Declared getSurrounding()
   {
      return ReflectionAdapter.getShadow(getExecutable().getDeclaringClass());
   }

   @Override
   public Package getPackage()
   {
      return getSurrounding().getPackage();
   }

   @Override
   public List<Generic> getGenerics()
   {
      return Arrays.stream(getExecutable().getTypeParameters())
                   .map(ReflectionAdapter::getShadow)
                   .map(Generic.class::cast)
                   .collect(collectingAndThen(toList(), Collections::unmodifiableList));
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
      return Optional.of(((Declared) ReflectionAdapter.getShadow(receiverType.getType())));
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

      if (otherSurrounding.isTypeKind(TypeKind.CLASS))
      {
         if (!method.isPublic() && !method.isProtected() && (!method.isPackagePrivate() || !method.getPackage().equals(getPackage())))
         {
            return false;
         }

         if (!getSurrounding().isTypeKind(TypeKind.CLASS))
         {
            return false;
         }
         Class otherSurroundingClass = Converter.convert(otherSurrounding).toClassOrThrow();
         Class surroundingClass = Converter.convert(getSurrounding()).toClassOrThrow();
         if (!surroundingClass.isSubtypeOf(otherSurroundingClass))
         {
            return false;
         }
      }
      if (otherSurrounding.isTypeKind(TypeKind.INTERFACE))
      {
         if (!method.isPublic())
         {
            return false;
         }

         if (!getSurrounding().isTypeKind(TypeKind.CLASS))
         {
            return false;
         }
         Interface otherSurroundingInterface = Converter.convert(otherSurrounding).toInterfaceOrThrow();
         Class surroundingClass = Converter.convert(getSurrounding()).toClassOrThrow();
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

   @Override
   public TypeKind getTypeKind()
   {
      if (getExecutable() instanceof java.lang.reflect.Method)
      {
         return TypeKind.METHOD;
      }
      return TypeKind.CONSTRUCTOR;
   }

   @Override
   public boolean representsSameType(Shadow shadow)
   {
      return shadow != null &&
             Converter.convert(shadow)
                      .toExecutable()
                      .map(executable1 -> executable1.getReturnType().representsSameType(getReturnType()))
                      .orElse(false);
   }

   public java.lang.reflect.Executable getExecutable()
   {
      return executable;
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getTypeKind(),
                          getName(),
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
      if (!(other instanceof Executable))
      {
         return false;
      }
      Executable otherExecutable = ((Executable) other);
      return Objects.equals(getName(), otherExecutable.getName()) &&
             Objects.equals(getTypeKind(), otherExecutable.getTypeKind()) &&
             Objects.equals(getParameters(), otherExecutable.getParameters()) &&
             Objects.equals(getModifiers(), otherExecutable.getModifiers()) &&
             Objects.equals(getParameterTypes(), otherExecutable.getParameterTypes());
   }

   public java.lang.reflect.Executable getReflection()
   {
      return getExecutable();
   }
}

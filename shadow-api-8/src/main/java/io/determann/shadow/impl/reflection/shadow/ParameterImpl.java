package io.determann.shadow.impl.reflection.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.converter.Converter;
import io.determann.shadow.api.modifier.Modifier;
import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.*;
import io.determann.shadow.impl.reflection.ReflectionUtil;

import java.util.*;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public class ParameterImpl implements Parameter
{
   private final java.lang.reflect.Parameter parameter;

   public ParameterImpl(java.lang.reflect.Parameter parameter)
   {
      this.parameter = parameter;
   }

   @Override
   public String getName()
   {
      return getParameter().getName();
   }

   @Override
   public List<AnnotationUsage> getAnnotationUsages()
   {
      return Arrays.stream(getParameter().getAnnotations())
                   .map(ReflectionAdapter::getAnnotationUsage)
                   .collect(collectingAndThen(toList(), Collections::unmodifiableList));
   }

   @Override
   public List<AnnotationUsage> getDirectAnnotationUsages()
   {
      return Arrays.stream(getParameter().getDeclaredAnnotations())
                   .map(ReflectionAdapter::getAnnotationUsage)
                   .collect(collectingAndThen(toList(), Collections::unmodifiableList));
   }

   @Override
   public Set<Modifier> getModifiers()
   {
      int modifiers = getParameter().getModifiers() & java.lang.reflect.Modifier.parameterModifiers();
      return ReflectionUtil.getModifiers(modifiers, false);
   }

   @Override
   public boolean isVarArgs()
   {
      return getParameter().isVarArgs();
   }

   @Override
   public TypeKind getTypeKind()
   {
      return TypeKind.PARAMETER;
   }

   @Override
   public boolean representsSameType(Shadow shadow)
   {
      return shadow != null &&
             Converter.convert(shadow).toParameter().map(parameter1 -> parameter1.getType().representsSameType(getType())).orElse(false);
   }
   @Override
   public boolean isSubtypeOf(Shadow shadow)
   {
      if (getType() instanceof Primitive)
      {
         Primitive primitive = ((Primitive) getType());
         return primitive.isSubtypeOf(shadow);
      }
      if (getType() instanceof Class)
      {
         Class aClass = ((Class) getType());
         return aClass.isSubtypeOf(shadow);
      }
      if (getType() instanceof Array)
      {
         Array array = ((Array) getType());
         return array.isSubtypeOf(shadow);
      }
      return false;
   }

   @Override
   public boolean isAssignableFrom(Shadow shadow)
   {
      if (getType() instanceof Primitive)
      {
         Primitive primitive = ((Primitive) getType());
         return primitive.isAssignableFrom(shadow);
      }
      if (getType() instanceof Class)
      {
         Class aClass = ((Class) getType());
         return aClass.isAssignableFrom(shadow);
      }
      return false;
   }

   @Override
   public Shadow getType()
   {
      return ReflectionAdapter.getShadow(getParameter().getParameterizedType());
   }

   @Override
   public Package getPackage()
   {
      return getSurrounding().getPackage();
   }

   @Override
   public Executable getSurrounding()
   {
      return ReflectionAdapter.getShadow(getParameter().getDeclaringExecutable());
   }

   public java.lang.reflect.Parameter getParameter()
   {
      return parameter;
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getTypeKind(),
                          getName(),
                          getSurrounding(),
                          isVarArgs());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof Parameter))
      {
         return false;
      }
      Parameter otherVariable = ((Parameter) other);
      return Objects.equals(getName(), otherVariable.getName()) &&
             Objects.equals(getType(), otherVariable.getType()) &&
             Objects.equals(getModifiers(), otherVariable.getModifiers()) &&
             Objects.equals(isVarArgs(), otherVariable.isVarArgs());
   }

   public java.lang.reflect.Parameter getReflection()
   {
      return parameter;
   }
}

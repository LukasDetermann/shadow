package io.determann.shadow.internal.reflection.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.modifier.Modifier;
import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.reflection.query.NameableReflection;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.*;
import io.determann.shadow.internal.reflection.ReflectionUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static io.determann.shadow.api.converter.Converter.convert;
import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;
import static io.determann.shadow.meta_meta.Operations.NAME;
import static io.determann.shadow.meta_meta.Provider.request;

public class ParameterImpl implements Parameter,
                                      NameableReflection
{
   private final java.lang.reflect.Parameter parameter;

   public ParameterImpl(java.lang.reflect.Parameter parameter)
   {
      this.parameter = parameter;
   }

   @Override
   public Module getModule()
   {
      return getSurrounding().getModule();
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
                   .map(ReflectionAdapter::generalize)
                   .toList();
   }

   @Override
   public List<AnnotationUsage> getDirectAnnotationUsages()
   {
      return Arrays.stream(getParameter().getDeclaredAnnotations())
                   .map(ReflectionAdapter::generalize)
                   .toList();
   }

   @Override
   public Set<Modifier> getModifiers()
   {
      int modifiers = getParameter().getModifiers() & java.lang.reflect.Modifier.parameterModifiers();
      return ReflectionUtil.getModifiers(modifiers, false, false);
   }

   @Override
   public boolean isVarArgs()
   {
      return getParameter().isVarArgs();
   }

   @Override
   public TypeKind getKind()
   {
      return TypeKind.PARAMETER;
   }

   @Override
   public boolean representsSameType(Shadow shadow)
   {
      return shadow != null &&
             convert(shadow).toParameter().map(parameter1 -> parameter1.getType().representsSameType(getType())).orElse(false);
   }
   @Override
   public boolean isSubtypeOf(Shadow shadow)
   {
      if (getType() instanceof Primitive primitive)
      {
         return primitive.isSubtypeOf(shadow);
      }
      if (getType() instanceof Class aClass)
      {
         return aClass.isSubtypeOf(shadow);
      }
      if (getType() instanceof Array array)
      {
         return array.isSubtypeOf(shadow);
      }
      return false;
   }

   @Override
   public boolean isAssignableFrom(Shadow shadow)
   {
      if (getType() instanceof Primitive primitive)
      {
         return primitive.isAssignableFrom(shadow);
      }
      if (getType() instanceof Class aClass)
      {
         return aClass.isAssignableFrom(shadow);
      }
      return false;
   }

   @Override
   public Shadow getType()
   {
      return ReflectionAdapter.generalize(getParameter().getParameterizedType());
   }

   @Override
   public Package getPackage()
   {
      return getSurrounding().getPackage();
   }

   @Override
   public Executable getSurrounding()
   {
      return ReflectionAdapter.generalize(getParameter().getDeclaringExecutable());
   }

   public java.lang.reflect.Parameter getParameter()
   {
      return parameter;
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getKind(),
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
      if (!(other instanceof Parameter otherVariable))
      {
         return false;
      }
      return request(otherVariable, NAME).map(name -> Objects.equals(getName(), name)).orElse(false) &&
             Objects.equals(getType(), otherVariable.getType()) &&
             Objects.equals(getModifiers(), otherVariable.getModifiers()) &&
             Objects.equals(isVarArgs(), otherVariable.isVarArgs());
   }

   public java.lang.reflect.Parameter getReflection()
   {
      return parameter;
   }

   @Override
   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
   }
}

package io.determann.shadow.internal.reflection.shadow.structure;

import io.determann.shadow.api.C;
import io.determann.shadow.api.Modifier;
import io.determann.shadow.api.query.Implementation;
import io.determann.shadow.api.query.Provider;
import io.determann.shadow.api.reflection.Adapter;
import io.determann.shadow.api.reflection.R;
import io.determann.shadow.internal.reflection.ReflectionUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static io.determann.shadow.api.query.Operations.*;
import static io.determann.shadow.api.query.Provider.requestOrEmpty;
import static io.determann.shadow.api.query.Provider.requestOrThrow;
import static io.determann.shadow.api.reflection.Adapter.IMPLEMENTATION;

public class ParameterImpl implements R.Parameter
{
   private final java.lang.reflect.Parameter parameter;

   public ParameterImpl(java.lang.reflect.Parameter parameter)
   {
      this.parameter = parameter;
   }

   @Override
   public R.Module getModule()
   {
      return getSurrounding().getModule();
   }

   @Override
   public String getName()
   {
      return getParameter().getName();
   }

   @Override
   public List<R.AnnotationUsage> getAnnotationUsages()
   {
      return Arrays.stream(getParameter().getAnnotations())
                   .map(Adapter::generalize)
                   .toList();
   }

   @Override
   public List<R.AnnotationUsage> getDirectAnnotationUsages()
   {
      return Arrays.stream(getParameter().getDeclaredAnnotations())
                   .map(Adapter::generalize)
                   .toList();
   }

   @Override
   public Set<Modifier> getModifiers()
   {
      int modifiers = getParameter().getModifiers() & java.lang.reflect.Modifier.parameterModifiers();
      return ReflectionUtil.getModifiers(modifiers, false, false, false, false);
   }

   @Override
   public boolean isVarArgs()
   {
      return getParameter().isVarArgs();
   }

   @Override
   public boolean isSubtypeOf(C.Type type)
   {
      if (getType() instanceof C.Primitive primitive)
      {
         return requestOrThrow(primitive, PRIMITIVE_IS_SUBTYPE_OF, type);
      }
      if (getType() instanceof C.Class aClass)
      {
         return requestOrThrow(aClass, DECLARED_IS_SUBTYPE_OF, type);
      }
      if (getType() instanceof C.Array array)
      {
         return requestOrThrow(array, ARRAY_IS_SUBTYPE_OF, type);
      }
      return false;
   }

   @Override
   public boolean isAssignableFrom(C.Type type)
   {
      if (getType() instanceof C.Primitive primitive)
      {
         return requestOrThrow(primitive, PRIMITIVE_IS_ASSIGNABLE_FROM, type);
      }
      if (getType() instanceof C.Class aClass)
      {
         return requestOrThrow(aClass, CLASS_IS_ASSIGNABLE_FROM, type);
      }
      return false;
   }

   @Override
   public R.VariableType getType()
   {
      return (R.VariableType) Adapter.generalize(getParameter().getParameterizedType());
   }

   @Override
   public R.Executable getSurrounding()
   {
      return Adapter.generalize(getParameter().getDeclaringExecutable());
   }

   public java.lang.reflect.Parameter getParameter()
   {
      return parameter;
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getName(),
                          getSurrounding());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof C.Parameter otherVariable))
      {
         return false;
      }
      return Provider.requestOrEmpty(otherVariable, NAMEABLE_GET_NAME).map(name -> Objects.equals(getName(), name)).orElse(false) &&
             Objects.equals(getType(), requestOrThrow(otherVariable, VARIABLE_GET_TYPE)) &&
             requestOrEmpty(otherVariable, MODIFIABLE_GET_MODIFIERS).map(modifiers -> Objects.equals(modifiers, getModifiers())).orElse(false);
   }

   public java.lang.reflect.Parameter getReflection()
   {
      return parameter;
   }

   @Override
   public Implementation getImplementation()
   {
      return IMPLEMENTATION;
   }
}

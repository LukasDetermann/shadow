package io.determann.shadow.internal.reflection.shadow.structure;

import io.determann.shadow.api.Implementation;
import io.determann.shadow.api.Provider;
import io.determann.shadow.api.reflection.R_Adapter;
import io.determann.shadow.api.reflection.shadow.R_AnnotationUsage;
import io.determann.shadow.api.reflection.shadow.structure.R_Executable;
import io.determann.shadow.api.reflection.shadow.structure.R_Module;
import io.determann.shadow.api.reflection.shadow.structure.R_Parameter;
import io.determann.shadow.api.reflection.shadow.type.R_Type;
import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.structure.C_Parameter;
import io.determann.shadow.api.shadow.type.C_Array;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Type;
import io.determann.shadow.api.shadow.type.primitive.C_Primitive;
import io.determann.shadow.internal.reflection.ReflectionUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.reflection.R_Adapter.IMPLEMENTATION;

public class ParameterImpl implements R_Parameter
{
   private final java.lang.reflect.Parameter parameter;

   public ParameterImpl(java.lang.reflect.Parameter parameter)
   {
      this.parameter = parameter;
   }

   @Override
   public R_Module getModule()
   {
      return getSurrounding().getModule();
   }

   @Override
   public String getName()
   {
      return getParameter().getName();
   }

   @Override
   public List<R_AnnotationUsage> getAnnotationUsages()
   {
      return Arrays.stream(getParameter().getAnnotations())
                   .map(R_Adapter::generalize)
                   .toList();
   }

   @Override
   public List<R_AnnotationUsage> getDirectAnnotationUsages()
   {
      return Arrays.stream(getParameter().getDeclaredAnnotations())
                   .map(R_Adapter::generalize)
                   .toList();
   }

   @Override
   public Set<C_Modifier> getModifiers()
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
   public boolean isSubtypeOf(C_Type type)
   {
      if (getType() instanceof C_Primitive primitive)
      {
         return requestOrThrow(primitive, PRIMITIVE_IS_SUBTYPE_OF, type);
      }
      if (getType() instanceof C_Class aClass)
      {
         return requestOrThrow(aClass, DECLARED_IS_SUBTYPE_OF, type);
      }
      if (getType() instanceof C_Array array)
      {
         return requestOrThrow(array, ARRAY_IS_SUBTYPE_OF, type);
      }
      return false;
   }

   @Override
   public boolean isAssignableFrom(C_Type type)
   {
      if (getType() instanceof C_Primitive primitive)
      {
         return requestOrThrow(primitive, PRIMITIVE_IS_ASSIGNABLE_FROM, type);
      }
      if (getType() instanceof C_Class aClass)
      {
         return requestOrThrow(aClass, CLASS_IS_ASSIGNABLE_FROM, type);
      }
      return false;
   }

   @Override
   public R_Type getType()
   {
      return R_Adapter.generalize(getParameter().getParameterizedType());
   }

   @Override
   public R_Executable getSurrounding()
   {
      return R_Adapter.generalize(getParameter().getDeclaringExecutable());
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
      if (!(other instanceof C_Parameter otherVariable))
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

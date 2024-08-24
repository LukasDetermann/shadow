package io.determann.shadow.internal.reflection.shadow.structure;

import io.determann.shadow.api.reflection.R_Adapter;
import io.determann.shadow.api.reflection.shadow.structure.R_Field;
import io.determann.shadow.api.reflection.shadow.structure.R_Module;
import io.determann.shadow.api.reflection.shadow.structure.R_Package;
import io.determann.shadow.api.reflection.shadow.type.R_Declared;
import io.determann.shadow.api.shadow.type.C_Array;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Declared;
import io.determann.shadow.api.shadow.type.C_Shadow;
import io.determann.shadow.api.shadow.type.primitive.C_Primitive;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;

public class FieldImpl extends ReflectionFieldImpl<C_Declared> implements R_Field
{
   public FieldImpl(java.lang.reflect.Field field)
   {
      super(field);
   }

   @Override
   public boolean isConstant()
   {
      return isFinal() && isStatic();
   }

   @Override
   public Object getConstantValue()
   {
      if (!isConstant() || (!getField().getType().isPrimitive() && !getField().getType().equals(String.class)))
      {
         return null;
      }
      try
      {
         return getField().get(null);
      }
      catch (IllegalAccessException e)
      {
         try
         {
            getField().setAccessible(true);
            return getField().get(null);
         }
         catch (IllegalAccessException i)
         {
            throw new RuntimeException(i);
         }
      }
   }

   @Override
   public boolean isSubtypeOf(C_Shadow shadow)
   {
      if (getType() instanceof C_Primitive primitive)
      {
         return requestOrThrow(primitive, PRIMITIVE_IS_SUBTYPE_OF, shadow);
      }
      if (getType() instanceof C_Class aClass)
      {
         return requestOrThrow(aClass, DECLARED_IS_SUBTYPE_OF, shadow);
      }
      if (getType() instanceof C_Array array)
      {
         return requestOrThrow(array, ARRAY_IS_SUBTYPE_OF, shadow);
      }
      return false;
   }

   @Override
   public boolean isAssignableFrom(C_Shadow shadow)
   {
      if (getType() instanceof C_Primitive primitive)
      {
         return requestOrThrow(primitive, PRIMITIVE_IS_ASSIGNABLE_FROM, shadow);
      }
      if (getType() instanceof C_Class aClass)
      {
         return requestOrThrow(aClass, CLASS_IS_ASSIGNABLE_FROM, shadow);
      }
      return false;
   }

   @Override
   public R_Package getPackage()
   {
      return (R_Package) requestOrThrow(getSurrounding(), DECLARED_GET_PACKAGE);
   }

   @Override
   public R_Declared getSurrounding()
   {
      return R_Adapter.generalize(getField().getDeclaringClass());
   }

   @Override
   public R_Module getModule()
   {
      return (R_Module) requestOrThrow(getSurrounding(), MODULE_ENCLOSED_GET_MODULE);
   }
}

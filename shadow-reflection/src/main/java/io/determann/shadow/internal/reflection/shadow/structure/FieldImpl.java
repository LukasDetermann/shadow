package io.determann.shadow.internal.reflection.shadow.structure;

import io.determann.shadow.api.C;
import io.determann.shadow.api.reflection.Adapter;
import io.determann.shadow.api.reflection.R;

import static io.determann.shadow.api.query.Operations.*;
import static io.determann.shadow.api.query.Provider.requestOrThrow;

public class FieldImpl extends ReflectionFieldImpl<C.Declared> implements R.Field
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
   public R.Declared getSurrounding()
   {
      return Adapter.generalize(getField().getDeclaringClass());
   }

   @Override
   public R.Module getModule()
   {
      return (R.Module) requestOrThrow(getSurrounding(), MODULE_ENCLOSED_GET_MODULE);
   }
}

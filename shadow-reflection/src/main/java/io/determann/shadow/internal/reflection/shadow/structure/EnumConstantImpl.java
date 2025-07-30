package io.determann.shadow.internal.reflection.shadow.structure;

import io.determann.shadow.api.C;
import io.determann.shadow.api.reflection.Adapter;
import io.determann.shadow.api.reflection.R;

import java.lang.reflect.Field;

import static io.determann.shadow.api.query.Operations.*;
import static io.determann.shadow.api.query.Provider.requestOrThrow;

public class EnumConstantImpl extends ReflectionFieldImpl<C.Enum> implements R.EnumConstant
{
   public EnumConstantImpl(Field field)
   {
      super(field);
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
   public R.Enum getSurrounding()
   {
      return Adapter.generalize(getField().getDeclaringClass());
   }

   @Override
   public R.Module getModule()
   {
      return (R.Module) requestOrThrow(getSurrounding(), MODULE_ENCLOSED_GET_MODULE);
   }
}

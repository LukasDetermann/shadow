package io.determann.shadow.internal.reflection.shadow.structure;

import io.determann.shadow.api.reflection.R_Adapter;
import io.determann.shadow.api.reflection.shadow.structure.R_EnumConstant;
import io.determann.shadow.api.reflection.shadow.structure.R_Module;
import io.determann.shadow.api.reflection.shadow.type.R_Enum;
import io.determann.shadow.api.shadow.type.C_Array;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Enum;
import io.determann.shadow.api.shadow.type.C_Type;
import io.determann.shadow.api.shadow.type.primitive.C_Primitive;

import java.lang.reflect.Field;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;

public class EnumConstantImpl extends ReflectionFieldImpl<C_Enum> implements R_EnumConstant
{
   public EnumConstantImpl(Field field)
   {
      super(field);
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
   public R_Enum getSurrounding()
   {
      return R_Adapter.generalize(getField().getDeclaringClass());
   }

   @Override
   public R_Module getModule()
   {
      return (R_Module) requestOrThrow(getSurrounding(), MODULE_ENCLOSED_GET_MODULE);
   }
}

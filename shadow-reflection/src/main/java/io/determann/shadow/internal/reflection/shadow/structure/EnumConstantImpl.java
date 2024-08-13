package io.determann.shadow.internal.reflection.shadow.structure;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.reflection.shadow.structure.EnumConstantReflection;
import io.determann.shadow.api.shadow.structure.EnumConstant;
import io.determann.shadow.api.shadow.structure.Module;
import io.determann.shadow.api.shadow.structure.Package;
import io.determann.shadow.api.shadow.type.Class;
import io.determann.shadow.api.shadow.type.Enum;
import io.determann.shadow.api.shadow.type.*;

import java.lang.reflect.Field;

import static io.determann.shadow.api.shadow.Operations.*;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;

public class EnumConstantImpl extends ReflectionFieldImpl<Enum> implements EnumConstantReflection
{
   public EnumConstantImpl(Field field)
   {
      super(field);
   }

   @Override
   public boolean isSubtypeOf(Shadow shadow)
   {
      if (getType() instanceof Primitive primitive)
      {
         return requestOrThrow(primitive, PRIMITIVE_IS_SUBTYPE_OF, shadow);
      }
      if (getType() instanceof Class aClass)
      {
         return requestOrThrow(aClass, DECLARED_IS_SUBTYPE_OF, shadow);
      }
      if (getType() instanceof Array array)
      {
         return requestOrThrow(array, ARRAY_IS_SUBTYPE_OF, shadow);
      }
      return false;
   }

   @Override
   public boolean isAssignableFrom(Shadow shadow)
   {
      if (getType() instanceof Primitive primitive)
      {
         return requestOrThrow(primitive, PRIMITIVE_IS_ASSIGNABLE_FROM, shadow);
      }
      if (getType() instanceof Class aClass)
      {
         return requestOrThrow(aClass, CLASS_IS_ASSIGNABLE_FROM, shadow);
      }
      return false;
   }

   @Override
   public Package getPackage()
   {
      return requestOrThrow(getSurrounding(), DECLARED_GET_PACKAGE);
   }

   @Override
   public Enum getSurrounding()
   {
      return ReflectionAdapter.generalize(getField().getDeclaringClass());
   }

   @Override
   public boolean representsSameType(Shadow shadow)
   {
      return shadow instanceof EnumConstant enumConstant &&
             requestOrThrow(requestOrThrow(enumConstant, VARIABLE_GET_TYPE), SHADOW_REPRESENTS_SAME_TYPE, getType());
   }

   @Override
   public Module getModule()
   {
      return requestOrThrow(getSurrounding(), MODULE_ENCLOSED_GET_MODULE);
   }
}

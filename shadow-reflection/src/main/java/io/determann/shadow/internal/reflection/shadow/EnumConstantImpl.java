package io.determann.shadow.internal.reflection.shadow;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.reflection.query.ModuleEnclosedReflection;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Enum;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.*;

import java.lang.reflect.Field;

import static io.determann.shadow.api.converter.Converter.convert;
import static io.determann.shadow.meta_meta.Operations.*;
import static io.determann.shadow.meta_meta.Provider.requestOrThrow;

public class EnumConstantImpl extends ReflectionFieldImpl<Enum> implements EnumConstant,
                                                                           ModuleEnclosedReflection
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
         return array.isSubtypeOf(shadow);
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
         return aClass.isAssignableFrom(shadow);
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
      return shadow != null &&
             convert(shadow).toEnumConstant()
                            .map(enumConstant -> requestOrThrow(enumConstant.getType(), SHADOW_REPRESENTS_SAME_TYPE, getType()))
                            .orElse(false);
   }

   @Override
   public Module getModule()
   {
      return requestOrThrow(getSurrounding(), MODULE_ENCLOSED_GET_MODULE);
   }
}

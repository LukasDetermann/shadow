package io.determann.shadow.internal.reflection.shadow;

import io.determann.shadow.api.converter.Converter;
import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Enum;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.*;

import java.lang.reflect.Field;

public class EnumConstantImpl extends ReflectionFieldImpl<Enum> implements EnumConstant
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
   public Package getPackage()
   {
      return getSurrounding().getPackage();
   }

   @Override
   public Enum getSurrounding()
   {
      return ReflectionAdapter.getShadow(getField().getDeclaringClass());
   }

   @Override
   public boolean representsSameType(Shadow shadow)
   {
      return shadow != null &&
             Converter.convert(shadow).toEnumConstant().map(enumConstant -> enumConstant.getType().representsSameType(getType())).orElse(false);
   }

   @Override
   public Module getModule()
   {
      return getSurrounding().getModule();
   }

   @Override
   public String getJavaDoc()
   {
      return null;
   }
}

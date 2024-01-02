package io.determann.shadow.impl.reflection.shadow;

import io.determann.shadow.api.converter.Converter;
import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Enum;
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
      if (getType() instanceof Primitive)
      {
         Primitive primitive = ((Primitive) getType());
         return primitive.isSubtypeOf(shadow);
      }
      if (getType() instanceof Class)
      {
         Class aClass = ((Class) getType());
         return aClass.isSubtypeOf(shadow);
      }
      if (getType() instanceof Array)
      {
         Array array = ((Array) getType());
         return array.isSubtypeOf(shadow);
      }
      return false;
   }

   @Override
   public boolean isAssignableFrom(Shadow shadow)
   {
      if (getType() instanceof Primitive)
      {
         Primitive primitive = ((Primitive) getType());
         return primitive.isAssignableFrom(shadow);
      }
      if (getType() instanceof Class)
      {
         Class aClass = ((Class) getType());
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
   public String getJavaDoc()
   {
      return null;
   }
}

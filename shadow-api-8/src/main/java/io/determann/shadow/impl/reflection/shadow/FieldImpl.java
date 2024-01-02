package io.determann.shadow.impl.reflection.shadow;

import io.determann.shadow.api.converter.Converter;
import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.*;

public class FieldImpl extends ReflectionFieldImpl<Declared> implements Field
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
   public Declared getSurrounding()
   {
      return ReflectionAdapter.getShadow(getField().getDeclaringClass());
   }

   @Override
   public boolean representsSameType(Shadow shadow)
   {
      return shadow != null && Converter.convert(shadow).toField().map(field -> field.representsSameType(getType())).orElse(false);
   }

   @Override
   public String getJavaDoc()
   {
      return null;
   }
}

package io.determann.shadow.internal.reflection.shadow;

import io.determann.shadow.api.converter.Converter;
import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.reflection.query.ModuleEnclosedReflection;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.*;

import static io.determann.shadow.meta_meta.Operations.*;
import static io.determann.shadow.meta_meta.Provider.requestOrThrow;

public class FieldImpl extends ReflectionFieldImpl<Declared> implements Field,
                                                                        ModuleEnclosedReflection
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
      if (getType() instanceof Primitive primitive)
      {
         return requestOrThrow(primitive, PRIMITIVE_IS_SUBTYPE_OF, shadow);
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
      return getSurrounding().getPackage();
   }

   @Override
   public Declared getSurrounding()
   {
      return ReflectionAdapter.generalize(getField().getDeclaringClass());
   }

   @Override
   public boolean representsSameType(Shadow shadow)
   {
      return shadow != null &&
             Converter.convert(shadow)
                      .toField()
                      .map(field -> requestOrThrow(field.getType(), SHADOW_REPRESENTS_SAME_TYPE, getType()))
                      .orElse(false);
   }

   @Override
   public Module getModule()
   {
      return requestOrThrow(getSurrounding(), MODULE_ENCLOSED_GET_MODULE);
   }

   @Override
   public String getJavaDoc()
   {
      return null;
   }
}

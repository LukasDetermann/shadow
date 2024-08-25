package io.determann.shadow.internal.reflection.shadow.type.primitive;

import io.determann.shadow.api.ImplementationDefined;
import io.determann.shadow.api.reflection.R_Adapter;
import io.determann.shadow.api.reflection.shadow.type.R_Array;
import io.determann.shadow.api.reflection.shadow.type.R_Class;
import io.determann.shadow.api.reflection.shadow.type.primitive.*;
import io.determann.shadow.api.shadow.type.C_Shadow;
import io.determann.shadow.api.shadow.type.primitive.*;
import io.determann.shadow.implementation.support.api.shadow.type.PrimitiveSupport;
import io.determann.shadow.internal.reflection.shadow.type.ClassImpl;

import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;

public abstract class PrimitiveImpl implements ImplementationDefined
{
   private final Class<?> aClass;
   private final Class<?> boxedClass;
   private final Class<?> primitiveType;
   private final String name;

   public static class R_booleanImpl extends PrimitiveImpl implements R_boolean
   {
      public R_booleanImpl()
      {
         super(Boolean.TYPE, Boolean.class, C_boolean.class, "boolean");
      }
   }

   public static class R_byteImpl extends PrimitiveImpl implements R_byte
   {
      public R_byteImpl()
      {
         super(Byte.TYPE, Byte.class, C_boolean.class, "byte");
      }
   }
   public static class R_charImpl extends PrimitiveImpl implements R_char
   {
      public R_charImpl()
      {
         super(Character.TYPE, Character.class, C_char.class, "char");
      }
   }

   public static class R_doubleImpl extends PrimitiveImpl implements R_double
   {
      public R_doubleImpl()
      {
         super(Double.TYPE, Double.class, C_double.class, "double");
      }
   }

   public static class R_floatImpl extends PrimitiveImpl implements R_float
   {
      public R_floatImpl()
      {
         super(Float.TYPE, Float.class, C_float.class, "float");
      }
   }

   public static class R_intImpl extends PrimitiveImpl implements R_int
   {
      public R_intImpl()
      {
         super(Integer.TYPE, Integer.class, C_int.class, "int");
      }
   }

   public static class R_longImpl extends PrimitiveImpl implements R_long
   {
      public R_longImpl()
      {
         super(Long.TYPE, Long.class, C_long.class, "long");
      }
   }
   public static class R_shortImpl extends PrimitiveImpl implements R_short
   {
      public R_shortImpl()
      {
         super(Short.TYPE, Short.class, C_short.class, "short");
      }
   }

   protected PrimitiveImpl(Class<?> aClass, Class<?> boxedClass, Class<?> primitiveType, String name)
   {
      this.primitiveType = primitiveType;
      this.boxedClass = boxedClass;
      this.name = name;
      this.aClass = aClass;
   }

   public boolean isSubtypeOf(C_Shadow shadow)
   {
      return false;
   }

   public boolean isAssignableFrom(C_Shadow shadow)
   {
      return shadow instanceof C_Primitive primitive && getaClass().isAssignableFrom(R_Adapter.particularize((R_Primitive) primitive));
   }

   public R_Class asBoxed()
   {
      return new ClassImpl(boxedClass);
   }

   public boolean representsSameType(C_Shadow shadow)
   {
      return primitiveType.isInstance(shadow);
   }

   public Class<?> getaClass()
   {
      return aClass;
   }

   public String getName()
   {
      return name;
   }

   public R_Array asArray()
   {
      return R_Adapter.generalize(aClass.arrayType());
   }

   @Override
   public int hashCode()
   {
      return PrimitiveSupport.hashCode((C_Primitive) this);
   }

   @Override
   public boolean equals(Object other)
   {
      return PrimitiveSupport.equals((C_Primitive) this, other);
   }

   @Override
   public String toString()
   {
      return PrimitiveSupport.toString((C_Primitive) this);
   }

   public Class<?> getReflection()
   {
      return getaClass();
   }


   @Override
   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
   }
}
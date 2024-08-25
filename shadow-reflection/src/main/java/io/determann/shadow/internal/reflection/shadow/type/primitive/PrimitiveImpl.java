package io.determann.shadow.internal.reflection.shadow.type.primitive;

import io.determann.shadow.api.ImplementationDefined;
import io.determann.shadow.api.reflection.R_Adapter;
import io.determann.shadow.api.reflection.shadow.type.R_Array;
import io.determann.shadow.api.reflection.shadow.type.R_Class;
import io.determann.shadow.api.reflection.shadow.type.primitive.*;
import io.determann.shadow.api.shadow.C_TypeKind;
import io.determann.shadow.api.shadow.type.C_Shadow;
import io.determann.shadow.api.shadow.type.primitive.C_Primitive;
import io.determann.shadow.implementation.support.api.shadow.type.PrimitiveSupport;
import io.determann.shadow.internal.reflection.shadow.type.ClassImpl;

import static io.determann.shadow.api.Operations.SHADOW_GET_KIND;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.shadow.C_TypeKind.*;
import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;

public abstract class PrimitiveImpl implements ImplementationDefined
{
   private final Class<?> aClass;
   private final Class<?> boxedClass;
   private final C_TypeKind typeKind;
   private final String name;

   public static class R_booleanImpl extends PrimitiveImpl implements R_boolean
   {

      public R_booleanImpl(Class<?> aClass)
      {
         super(aClass, Boolean.class, BOOLEAN, "boolean");
      }

   }
   public static class R_byteImpl extends PrimitiveImpl implements R_byte
   {

      public R_byteImpl(Class<?> aClass)
      {
         super(aClass, Byte.class, BYTE, "byte");
      }

   }
   public static class R_charImpl extends PrimitiveImpl implements R_char
   {

      public R_charImpl(Class<?> aClass)
      {
         super(aClass, Character.class, CHAR, "char");
      }

   }
   public static class R_doubleImpl extends PrimitiveImpl implements R_double
   {

      public R_doubleImpl(Class<?> aClass)
      {
         super(aClass, Double.class, DOUBLE, "double");
      }

   }
   public static class R_floatImpl extends PrimitiveImpl implements R_float
   {

      public R_floatImpl(Class<?> aClass)
      {
         super(aClass, Float.class, FLOAT, "float");
      }

   }
   public static class R_intImpl extends PrimitiveImpl implements R_int
   {

      public R_intImpl(Class<?> aClass)
      {
         super(aClass, Integer.class, INT, "int");
      }

   }
   public static class R_longImpl extends PrimitiveImpl implements R_long
   {

      public R_longImpl(Class<?> aClass)
      {
         super(aClass, Long.class, LONG, "long");
      }

   }
   public static class R_shortImpl extends PrimitiveImpl implements R_short
   {

      public R_shortImpl(Class<?> aClass)
      {
         super(aClass, Short.class, SHORT, "short");
      }

   }

   protected PrimitiveImpl(Class<?> aClass, Class<?> boxedClass, C_TypeKind typeKind, String name)
   {
      this.boxedClass = boxedClass;
      this.typeKind = typeKind;
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
      return getKind().equals(requestOrThrow(shadow, SHADOW_GET_KIND));
   }

   public C_TypeKind getKind()
   {
      return typeKind;
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
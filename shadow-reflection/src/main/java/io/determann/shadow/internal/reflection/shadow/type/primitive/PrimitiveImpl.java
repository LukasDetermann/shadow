package io.determann.shadow.internal.reflection.shadow.type.primitive;

import io.determann.shadow.api.C;
import io.determann.shadow.api.query.Implementation;
import io.determann.shadow.api.query.ImplementationDefined;
import io.determann.shadow.api.reflection.Adapter;
import io.determann.shadow.api.reflection.R;
import io.determann.shadow.implementation.support.api.shadow.type.PrimitiveSupport;
import io.determann.shadow.internal.reflection.shadow.type.ClassImpl;

import static io.determann.shadow.api.reflection.Adapter.IMPLEMENTATION;

public abstract class PrimitiveImpl implements ImplementationDefined
{
   private final Class<?> aClass;
   private final Class<?> boxedClass;
   private final Class<?> primitiveType;
   private final String name;

   public static class R_booleanImpl extends PrimitiveImpl implements R.boolean_
   {
      public R_booleanImpl()
      {
         super(Boolean.TYPE, Boolean.class, C.boolean_.class, "boolean");
      }
   }

   public static class R_byteImpl extends PrimitiveImpl implements R.byte_
   {
      public R_byteImpl()
      {
         super(Byte.TYPE, Byte.class, C.boolean_.class, "byte");
      }
   }
   public static class R_charImpl extends PrimitiveImpl implements R.char_
   {
      public R_charImpl()
      {
         super(Character.TYPE, Character.class, C.char_.class, "char");
      }
   }

   public static class R_doubleImpl extends PrimitiveImpl implements R.double_
   {
      public R_doubleImpl()
      {
         super(Double.TYPE, Double.class, C.double_.class, "double");
      }
   }

   public static class R_floatImpl extends PrimitiveImpl implements R.float_
   {
      public R_floatImpl()
      {
         super(Float.TYPE, Float.class, C.float_.class, "float");
      }
   }

   public static class R_intImpl extends PrimitiveImpl implements R.int_
   {
      public R_intImpl()
      {
         super(Integer.TYPE, Integer.class, C.int_.class, "int");
      }
   }

   public static class R_longImpl extends PrimitiveImpl implements R.long_
   {
      public R_longImpl()
      {
         super(Long.TYPE, Long.class, C.long_.class, "long");
      }
   }
   public static class R_shortImpl extends PrimitiveImpl implements R.short_
   {
      public R_shortImpl()
      {
         super(Short.TYPE, Short.class, C.short_.class, "short");
      }
   }

   protected PrimitiveImpl(Class<?> aClass, Class<?> boxedClass, Class<?> primitiveType, String name)
   {
      this.primitiveType = primitiveType;
      this.boxedClass = boxedClass;
      this.name = name;
      this.aClass = aClass;
   }

   public boolean isSubtypeOf(C.Type type)
   {
      return false;
   }

   public boolean isAssignableFrom(C.Type type)
   {
      return type instanceof C.Primitive primitive && getaClass().isAssignableFrom(Adapter.particularize((R.Primitive) primitive));
   }

   public R.Class asBoxed()
   {
      return new ClassImpl(boxedClass);
   }

   public boolean representsSameType(C.Type type)
   {
      return primitiveType.isInstance(type);
   }

   public Class<?> getaClass()
   {
      return aClass;
   }

   public String getName()
   {
      return name;
   }

   public R.Array asArray()
   {
      return Adapter.generalize(aClass.arrayType());
   }

   @Override
   public int hashCode()
   {
      return PrimitiveSupport.hashCode((C.Primitive) this);
   }

   @Override
   public boolean equals(Object other)
   {
      return PrimitiveSupport.equals((C.Primitive) this, other);
   }

   @Override
   public String toString()
   {
      return PrimitiveSupport.toString((C.Primitive) this);
   }

   public Class<?> getReflection()
   {
      return getaClass();
   }


   @Override
   public Implementation getImplementation()
   {
      return IMPLEMENTATION;
   }
}
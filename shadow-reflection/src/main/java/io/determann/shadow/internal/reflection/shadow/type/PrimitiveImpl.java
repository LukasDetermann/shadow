package io.determann.shadow.internal.reflection.shadow.type;

import io.determann.shadow.api.reflection.R_Adapter;
import io.determann.shadow.api.reflection.shadow.type.R_Array;
import io.determann.shadow.api.reflection.shadow.type.R_Class;
import io.determann.shadow.api.reflection.shadow.type.primitive.*;
import io.determann.shadow.api.shadow.C_TypeKind;
import io.determann.shadow.api.shadow.type.C_Shadow;
import io.determann.shadow.api.shadow.type.primitive.C_Primitive;
import io.determann.shadow.implementation.support.api.shadow.type.PrimitiveSupport;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static io.determann.shadow.api.Operations.SHADOW_GET_KIND;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.shadow.C_TypeKind.*;
import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;

public class PrimitiveImpl implements R_boolean,
                                      R_byte,
                                      R_double,
                                      R_float,
                                      R_int,
                                      R_long,
                                      R_short
{
   private final java.lang.Class<?> aClass;
   private static final Map<java.lang.Class<?>, C_TypeKind> CLASS_KIND_MAP = new HashMap<>();

   static
   {
      CLASS_KIND_MAP.put(Boolean.TYPE, BOOLEAN);
      CLASS_KIND_MAP.put(Byte.TYPE, BYTE);
      CLASS_KIND_MAP.put(Character.TYPE, CHAR);
      CLASS_KIND_MAP.put(Short.TYPE, SHORT);
      CLASS_KIND_MAP.put(Integer.TYPE, INT);
      CLASS_KIND_MAP.put(Long.TYPE, LONG);
      CLASS_KIND_MAP.put(Float.TYPE, FLOAT);
      CLASS_KIND_MAP.put(Double.TYPE, DOUBLE);
   }

   private static final Map<java.lang.Class<?>, java.lang.Class<?>> PRIMITIVE_BOXED_MAP = new HashMap<>();

   static
   {
      PRIMITIVE_BOXED_MAP.put(Boolean.TYPE, Boolean.class);
      PRIMITIVE_BOXED_MAP.put(Byte.TYPE, Byte.class);
      PRIMITIVE_BOXED_MAP.put(Character.TYPE, Character.class);
      PRIMITIVE_BOXED_MAP.put(Short.TYPE, Short.class);
      PRIMITIVE_BOXED_MAP.put(Integer.TYPE, Integer.class);
      PRIMITIVE_BOXED_MAP.put(Long.TYPE, Long.class);
      PRIMITIVE_BOXED_MAP.put(Float.TYPE, Float.class);
      PRIMITIVE_BOXED_MAP.put(Double.TYPE, Double.class);
   }

   public PrimitiveImpl(java.lang.Class<?> aClass)
   {
      this.aClass = aClass;
   }

   @Override
   public boolean isSubtypeOf(C_Shadow shadow)
   {
      return false;
   }

   @Override
   public boolean isAssignableFrom(C_Shadow shadow)
   {
      return shadow instanceof C_Primitive primitive && getaClass().isAssignableFrom(R_Adapter.particularize((R_Primitive) primitive));
   }

   @Override
   public R_Class asBoxed()
   {
      return new ClassImpl(Objects.requireNonNull(PRIMITIVE_BOXED_MAP.get(getaClass())));
   }

   @Override
   public boolean representsSameType(C_Shadow shadow)
   {
      return getKind().equals(requestOrThrow(shadow, SHADOW_GET_KIND));
   }

   @Override
   public C_TypeKind getKind()
   {
      return Objects.requireNonNull(CLASS_KIND_MAP.get(getaClass()));
   }

   public java.lang.Class<?> getaClass()
   {
      return aClass;
   }

   @Override
   public String getName()
   {
      return switch (getKind())
      {
         case BOOLEAN -> "boolean";
         case BYTE -> "byte";
         case SHORT -> "short";
         case INT -> "int";
         case LONG -> "long";
         case CHAR -> "char";
         case FLOAT -> "float";
         case DOUBLE -> "double";
         default -> throw new IllegalStateException();
      };
   }

   @Override
   public R_Array asArray()
   {
      return R_Adapter.generalize(aClass.arrayType());
   }

   @Override
   public int hashCode()
   {
      return PrimitiveSupport.hashCode(this);
   }

   @Override
   public boolean equals(Object other)
   {
      return PrimitiveSupport.equals(this, other);
   }

   @Override
   public String toString()
   {
      return PrimitiveSupport.toString(this);
   }

   public java.lang.Class<?> getReflection()
   {
      return getaClass();
   }


   @Override
   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
   }
}
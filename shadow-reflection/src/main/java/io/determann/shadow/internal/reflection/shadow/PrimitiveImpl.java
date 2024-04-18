package io.determann.shadow.internal.reflection.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Primitive;
import io.determann.shadow.api.shadow.Shadow;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static io.determann.shadow.api.TypeKind.*;
import static io.determann.shadow.api.converter.Converter.convert;
import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;

public class PrimitiveImpl implements Primitive
{
   private final java.lang.Class<?> aClass;
   private static final Map<java.lang.Class<?>, TypeKind> CLASS_KIND_MAP = new HashMap<>();
   static {
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
   static {
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
   public boolean isSubtypeOf(Shadow shadow)
   {
      return false;
   }

   @Override
   public boolean isAssignableFrom(Shadow shadow)
   {
      return convert(shadow)
            .toPrimitive()
            .map(primitive -> getaClass().isAssignableFrom(ReflectionAdapter.particularize(primitive)))
            .orElse(false);
   }

   @Override
   public Class asBoxed()
   {
      return new ClassImpl(Objects.requireNonNull(PRIMITIVE_BOXED_MAP.get(getaClass())));
   }

   @Override
   public boolean representsSameType(Shadow shadow)
   {
      return getKind().equals(shadow.getKind());
   }

   @Override
   public TypeKind getKind()
   {
      return Objects.requireNonNull(CLASS_KIND_MAP.get(getaClass()));
   }

   public java.lang.Class<?> getaClass()
   {
      return aClass;
   }

   @Override
   public int hashCode()
   {
      return Objects.hashCode(getKind());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof Primitive otherPrimitive))
      {
         return false;
      }
      return Objects.equals(getKind(), otherPrimitive.getKind());
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
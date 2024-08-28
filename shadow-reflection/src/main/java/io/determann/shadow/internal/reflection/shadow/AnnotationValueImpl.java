package io.determann.shadow.internal.reflection.shadow;

import io.determann.shadow.api.reflection.R_Adapter;
import io.determann.shadow.api.reflection.shadow.R_AnnotationUsage;
import io.determann.shadow.api.reflection.shadow.R_AnnotationValue;
import io.determann.shadow.api.reflection.shadow.structure.R_EnumConstant;
import io.determann.shadow.api.reflection.shadow.type.R_Type;
import io.determann.shadow.api.shadow.C_AnnotationValue;
import io.determann.shadow.implementation.support.api.shadow.AnnotationValueSupport;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;
import static java.util.Arrays.stream;

public abstract class AnnotationValueImpl
{
   private final boolean isDefault;

   public static R_AnnotationValue create(Object value, boolean isDefault)
   {
      if (value instanceof String)
      {
         return new StringValueImpl(value, isDefault);
      }
      if (value instanceof Boolean)
      {
         return new BooleanValueImpl(value, isDefault);
      }
      if (value instanceof Byte)
      {
         return new ByteValueImpl(value, isDefault);
      }
      if (value instanceof Short)
      {
         return new ShortValueImpl(value, isDefault);
      }
      if (value instanceof Integer)
      {
         return new IntegerValueImpl(value, isDefault);
      }
      if (value instanceof Long)
      {
         return new LongValueImpl(value, isDefault);
      }
      if (value instanceof Character)
      {
         return new CharacterValueImpl(value, isDefault);
      }
      if (value instanceof Float)
      {
         return new FloatValueImpl(value, isDefault);
      }
      if (value instanceof Double)
      {
         return new DoubleValueImpl(value, isDefault);
      }
      if (value instanceof Class<?>)
      {
         return new TypeValueImpl(value, isDefault);
      }
      if (value instanceof Enum<?>)
      {
         return new EnumConstantValueImpl(value, isDefault);
      }
      if (value.getClass().isArray())
      {
         return new ValuesImpl(value, isDefault);
      }
      if (value instanceof Annotation)
      {
         return new AnnotationUsageValueImpl(value, isDefault);
      }
      throw new IllegalStateException();
   }

   private static class StringValueImpl extends AnnotationValueImpl implements R_AnnotationValue.StringValue
   {
      private final Object annotationValue;

      StringValueImpl(Object annotationValue, boolean defaultValue)
      {
         super(defaultValue);
         this.annotationValue = annotationValue;
      }

      @Override
      public String getValue()
      {
         return ((String) annotationValue);
      }
   }

   private static class BooleanValueImpl extends AnnotationValueImpl implements R_AnnotationValue.BooleanValue
   {
      private final Object annotationValue;

      BooleanValueImpl(Object annotationValue, boolean defaultValue)
      {
         super(defaultValue);
         this.annotationValue = annotationValue;
      }

      @Override
      public Boolean getValue()
      {
         return ((Boolean) annotationValue);
      }
   }

   private static class ByteValueImpl extends AnnotationValueImpl implements R_AnnotationValue.ByteValue
   {
      private final Object annotationValue;

      ByteValueImpl(Object annotationValue, boolean defaultValue)
      {
         super(defaultValue);
         this.annotationValue = annotationValue;
      }

      @Override
      public Byte getValue()
      {
         return ((Byte) annotationValue);
      }
   }

   private static class ShortValueImpl extends AnnotationValueImpl implements R_AnnotationValue.ShortValue
   {
      private final Object annotationValue;

      ShortValueImpl(Object annotationValue, boolean defaultValue)
      {
         super(defaultValue);
         this.annotationValue = annotationValue;
      }

      @Override
      public Short getValue()
      {
         return ((Short) annotationValue);
      }
   }

   private static class IntegerValueImpl extends AnnotationValueImpl implements R_AnnotationValue.IntegerValue
   {
      private final Object annotationValue;

      IntegerValueImpl(Object annotationValue, boolean defaultValue)
      {
         super(defaultValue);
         this.annotationValue = annotationValue;
      }

      @Override
      public Integer getValue()
      {
         return ((Integer) annotationValue);
      }
   }

   private static class LongValueImpl extends AnnotationValueImpl implements R_AnnotationValue.LongValue
   {
      private final Object annotationValue;

      LongValueImpl(Object annotationValue, boolean defaultValue)
      {
         super(defaultValue);
         this.annotationValue = annotationValue;
      }

      @Override
      public Long getValue()
      {
         return ((Long) annotationValue);
      }
   }

   private static class CharacterValueImpl extends AnnotationValueImpl implements R_AnnotationValue.CharacterValue
   {
      private final Object annotationValue;

      CharacterValueImpl(Object annotationValue, boolean defaultValue)
      {
         super(defaultValue);
         this.annotationValue = annotationValue;
      }

      @Override
      public Character getValue()
      {
         return ((Character) annotationValue);
      }
   }

   private static class FloatValueImpl extends AnnotationValueImpl implements R_AnnotationValue.FloatValue
   {
      private final Object annotationValue;

      FloatValueImpl(Object annotationValue, boolean defaultValue)
      {
         super(defaultValue);
         this.annotationValue = annotationValue;
      }

      @Override
      public Float getValue()
      {
         return ((Float) annotationValue);
      }
   }

   private static class DoubleValueImpl extends AnnotationValueImpl implements R_AnnotationValue.DoubleValue
   {
      private final Object annotationValue;

      DoubleValueImpl(Object annotationValue, boolean defaultValue)
      {
         super(defaultValue);
         this.annotationValue = annotationValue;
      }

      @Override
      public Double getValue()
      {
         return ((Double) annotationValue);
      }
   }

   private static class TypeValueImpl extends AnnotationValueImpl implements R_AnnotationValue.TypeValue
   {
      private final Object annotationValue;

      TypeValueImpl(Object annotationValue, boolean defaultValue)
      {
         super(defaultValue);
         this.annotationValue = annotationValue;
      }

      @Override
      public R_Type getValue()
      {
         return R_Adapter.generalize(((Class<?>) annotationValue));
      }
   }

   private static class EnumConstantValueImpl extends AnnotationValueImpl implements R_AnnotationValue.EnumConstantValue
   {
      private final Object annotationValue;

      EnumConstantValueImpl(Object annotationValue, boolean defaultValue)
      {
         super(defaultValue);
         this.annotationValue = annotationValue;
      }

      @Override
      public R_EnumConstant getValue()
      {
         return R_Adapter.generalize((Enum<?>) annotationValue);
      }
   }

   private static class AnnotationUsageValueImpl extends AnnotationValueImpl implements R_AnnotationValue.AnnotationUsageValue
   {
      private final Object annotationValue;

      AnnotationUsageValueImpl(Object annotationValue, boolean defaultValue)
      {
         super(defaultValue);
         this.annotationValue = annotationValue;
      }

      @Override
      public R_AnnotationUsage getValue()
      {
         return new AnnotationUsageImpl(((Annotation) annotationValue));
      }
   }

   private static class ValuesImpl extends AnnotationValueImpl implements R_AnnotationValue.Values
   {
      private final Object annotationValue;

      ValuesImpl(Object annotationValue, boolean defaultValue)
      {
         super(defaultValue);
         this.annotationValue = annotationValue;
      }

      @Override
      public List<R_AnnotationValue> getValue()
      {
         Class<?> componentType = annotationValue.getClass().getComponentType();
         if (!componentType.isPrimitive())
         {
            return stream((Object[]) annotationValue).map(o -> create(o, isDefault()))
                                                     .map(R_AnnotationValue.class::cast)
                                                     .toList();
         }
         if (componentType.equals(boolean.class))
         {
            List<R_AnnotationValue> list = new ArrayList<>();
            for (Boolean o : (boolean[]) annotationValue)
            {
               list.add(create(o, isDefault()));
            }
            return Collections.unmodifiableList(list);
         }
         if (componentType.equals(byte.class))
         {
            List<R_AnnotationValue> list = new ArrayList<>();
            for (Byte o : (byte[]) annotationValue)
            {
               list.add(create(o, isDefault()));
            }
            return Collections.unmodifiableList(list);
         }
         if (componentType.equals(short.class))
         {
            List<R_AnnotationValue> list = new ArrayList<>();
            for (Short o : (short[]) annotationValue)
            {
               list.add(create(o, isDefault()));
            }
            return Collections.unmodifiableList(list);
         }
         if (componentType.equals(int.class))
         {
            return stream((int[]) annotationValue).boxed()
                                        .map(o -> create(o, isDefault()))
                                        .map(R_AnnotationValue.class::cast)
                                        .toList();
         }
         if (componentType.equals(long.class))
         {
            return stream((long[]) annotationValue).boxed()
                                         .map(o -> create(o, isDefault()))
                                         .map(R_AnnotationValue.class::cast)
                                         .toList();
         }
         if (componentType.equals(char.class))
         {
            List<R_AnnotationValue> list = new ArrayList<>();
            for (Character o : (char[]) annotationValue)
            {
               list.add(create(o, isDefault()));
            }
            return Collections.unmodifiableList(list);
         }
         if (componentType.equals(float.class))
         {
            List<R_AnnotationValue> list = new ArrayList<>();
            for (Float o : (float[]) annotationValue)
            {
               list.add(create(o, isDefault()));
            }
            return Collections.unmodifiableList(list);
         }
         if (componentType.equals(double.class))
         {
            return stream((double[]) annotationValue).boxed()
                                           .map(o -> create(o, isDefault()))
                                           .map(R_AnnotationValue.class::cast)
                                           .toList();
         }
         throw new IllegalStateException();
      }
   }

   private AnnotationValueImpl(boolean isDefault)
   {
      this.isDefault = isDefault;
   }

   public boolean isDefault()
   {
      return isDefault;
   }

   public abstract Object getValue();

   private <VALUE extends C_AnnotationValue, T> T getSave(Class<VALUE> valueClass, Class<T> tClass)
   {
      if (valueClass.isInstance(this))
      {
         //noinspection unchecked
         return ((T) getValue());
      }
      throw new IllegalStateException(valueClass.getName() + " does not contain values of type " + tClass.getName());
   }
   
   @Override
   public boolean equals(Object other)
   {
      return AnnotationValueSupport.equals((C_AnnotationValue) this, other);
   }

   @Override
   public int hashCode()
   {
      return AnnotationValueSupport.hashCode(((C_AnnotationValue) this));
   }

   @Override
   public String toString()
   {
      return AnnotationValueSupport.toString((C_AnnotationValue) this);
   }

   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
   }
}

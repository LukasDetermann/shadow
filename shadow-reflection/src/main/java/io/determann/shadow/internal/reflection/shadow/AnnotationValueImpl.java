package io.determann.shadow.internal.reflection.shadow;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.shadow.annotationusage.AnnotationUsage;
import io.determann.shadow.api.shadow.annotationusage.AnnotationValue;
import io.determann.shadow.api.shadow.annotationusage.AnnotationValueConsumer;
import io.determann.shadow.api.shadow.annotationusage.AnnotationValueMapper;
import io.determann.shadow.api.shadow.structure.EnumConstant;
import io.determann.shadow.api.shadow.type.Shadow;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.Arrays.stream;

public class AnnotationValueImpl implements AnnotationValue
{
   private final Object value;
   private final boolean isDefault;

   public AnnotationValueImpl(Object value, boolean isDefault)
   {
      this.value = value;
      this.isDefault = isDefault;
   }

   @Override
   public boolean isDefaultValue()
   {
      return isDefault;
   }

   @Override
   public Object getValue()
   {
      return value;
   }

   @Override
   public String asString()
   {
      return ((String) value);
   }

   @Override
   public Boolean asBoolean()
   {
      return ((Boolean) value);
   }

   @Override
   public Byte asByte()
   {
      return ((Byte) value);
   }

   @Override
   public Short asShort()
   {
      return ((Short) value);
   }

   @Override
   public Integer asInteger()
   {
      return ((Integer) value);
   }

   @Override
   public Long asLong()
   {
      return ((Long) value);
   }

   @Override
   public Character asCharacter()
   {
      return ((Character) value);
   }

   @Override
   public Float asFloat()
   {
      return ((Float) value);
   }

   @Override
   public Double asDouble()
   {
      return ((Double) value);
   }

   @Override
   public Shadow asType()
   {
      return ReflectionAdapter.generalize(((Class<?>) value));
   }

   @Override
   public EnumConstant asEnumConstant()
   {
      return ReflectionAdapter.generalize((Enum<?>) value);
   }

   @Override
   public AnnotationUsage asAnnotationUsage()
   {
      return new AnnotationUsageImpl(((Annotation) value));
   }

   /**
    * box primitives for refection or unbox wrappers for annotation processing
    */
   @Override
   public List<AnnotationValue> asListOfValues()
   {
      Class<?> componentType = value.getClass().getComponentType();
      if (!componentType.isPrimitive())
      {
         return stream((Object[])value).map(o -> new AnnotationValueImpl(o, isDefaultValue())).map(AnnotationValue.class::cast).toList();
      }
      if (componentType.equals(boolean.class))
      {
         List<AnnotationValue> list = new ArrayList<>();
         for (Boolean o : (boolean[]) value)
         {
            list.add(new AnnotationValueImpl(o, isDefaultValue()));
         }
         return Collections.unmodifiableList(list);
      }
      if (componentType.equals(byte.class))
      {
         List<AnnotationValue> list = new ArrayList<>();
         for (Byte o : (byte[]) value)
         {
            list.add(new AnnotationValueImpl(o, isDefaultValue()));
         }
         return Collections.unmodifiableList(list);
      }
      if (componentType.equals(short.class))
      {
         List<AnnotationValue> list = new ArrayList<>();
         for (Short o : (short[]) value)
         {
            list.add(new AnnotationValueImpl(o, isDefaultValue()));
         }
         return Collections.unmodifiableList(list);
      }
      if (componentType.equals(int.class))
      {
         return stream((int[])value).boxed().map(o -> new AnnotationValueImpl(o, isDefaultValue())).map(AnnotationValue.class::cast).toList();
      }
      if (componentType.equals(long.class))
      {
         return stream((long[]) value).boxed().map(o -> new AnnotationValueImpl(o, isDefaultValue())).map(AnnotationValue.class::cast).toList();
      }
      if (componentType.equals(char.class))
      {
         List<AnnotationValue> list = new ArrayList<>();
         for (Character o : (char[]) value)
         {
            list.add(new AnnotationValueImpl(o, isDefaultValue()));
         }
         return Collections.unmodifiableList(list);
      }
      if (componentType.equals(float.class))
      {
         List<AnnotationValue> list = new ArrayList<>();
         for (Float o : (float[]) value)
         {
            list.add(new AnnotationValueImpl(o, isDefaultValue()));
         }
         return Collections.unmodifiableList(list);
      }
      if (componentType.equals(double.class))
      {
         return stream((double[])value).boxed().map(o -> new AnnotationValueImpl(o, isDefaultValue())).map(AnnotationValue.class::cast).toList();
      }
      throw new IllegalStateException();
   }

   @Override
   public void consume(AnnotationValueConsumer consumer)
   {
      if (value instanceof String)
      {
         consumer.string(asString());
      }
      else if (value instanceof Boolean)
      {
         consumer.aBoolean(asBoolean());
      }
      else if (value instanceof Byte)
      {
         consumer.aByte(asByte());
      }
      else if (value instanceof Short)
      {
         consumer.aShort(asShort());
      }
      else if (value instanceof Integer)
      {
         consumer.integer(asInteger());
      }
      else if (value instanceof Long)
      {
         consumer.aLong(asLong());
      }
      else if (value instanceof Character)
      {
         consumer.character(asCharacter());
      }
      else if (value instanceof Float)
      {
         consumer.aFloat(asFloat());
      }
      else if (value instanceof Double)
      {
         consumer.aDouble(asDouble());
      }
      else if (value instanceof Class<?>)
      {
         consumer.type(asType());
      }
      else if (value instanceof Enum<?>)
      {
         consumer.enumConstant(asEnumConstant());
      }
      else if (value.getClass().isArray())
      {
         consumer.values(asListOfValues());
      }
      if (value instanceof Annotation)
      {
         consumer.annotationUsage(asAnnotationUsage());
      }
      throw new IllegalStateException();
   }

   @Override
   public <T> T map(AnnotationValueMapper<T> mapper)
   {
      if (value instanceof String)
      {
         return mapper.string(asString());
      }
      if (value instanceof Boolean)
      {
         return mapper.aBoolean(asBoolean());
      }
      if (value instanceof Byte)
      {
         return mapper.aByte(asByte());
      }
      if (value instanceof Short)
      {
         return mapper.aShort(asShort());
      }
      if (value instanceof Integer)
      {
         return mapper.integer(asInteger());
      }
      if (value instanceof Long)
      {
         return mapper.aLong(asLong());
      }
      if (value instanceof Character)
      {
         return mapper.character(asCharacter());
      }
      if (value instanceof Float)
      {
         return mapper.aFloat(asFloat());
      }
      if (value instanceof Double)
      {
         return mapper.aDouble(asDouble());
      }
      if (value instanceof Class<?>)
      {
         return mapper.type(asType());
      }
      if (value instanceof Enum<?>)
      {
         return mapper.enumConstant(asEnumConstant());
      }
      if (value.getClass().isArray())
      {
         return mapper.values(asListOfValues());
      }
      if (value instanceof Annotation)
      {
         return mapper.annotationUsage(asAnnotationUsage());
      }
      throw new IllegalStateException();
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o)
      {
         return true;
      }
      if (!(o instanceof AnnotationValue that))
      {
         return false;
      }
      return isDefaultValue() == that.isDefaultValue() && Objects.equals(getValue(), that.getValue());
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(value, isDefault);
   }
}

package io.determann.shadow.impl.lang_model.annotationvalue;

import io.determann.shadow.api.annotationvalue.AnnotationValue;
import io.determann.shadow.api.annotationvalue.AnnotationValueConsumer;
import io.determann.shadow.api.annotationvalue.AnnotationValueMapper;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.lang_model.MirrorAdapter;
import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.EnumConstant;
import io.determann.shadow.api.shadow.Shadow;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class AnnotationValueImpl implements AnnotationValue
{
   private final LangModelContext context;
   private final javax.lang.model.element.AnnotationValue annotationValue;
   private final boolean defaultValue;

   AnnotationValueImpl(LangModelContext context, javax.lang.model.element.AnnotationValue annotationValue, boolean defaultValue)
   {
      this.context = context;
      this.annotationValue = annotationValue;
      this.defaultValue = defaultValue;
   }

   @Override
   public Object getValue()
   {
      return annotationValue;
   }

   @Override
   public String asString()
   {
      return ((String) annotationValue.getValue());
   }

   @Override
   public Boolean asBoolean()
   {
      return ((Boolean) annotationValue.getValue());
   }

   @Override
   public Byte asByte()
   {
      return ((Byte) annotationValue.getValue());
   }

   @Override
   public Short asShort()
   {
      return ((Short) annotationValue.getValue());
   }

   @Override
   public Integer asInteger()
   {
      return ((Integer) annotationValue.getValue());
   }

   @Override
   public Long asLong()
   {
      return ((Long) annotationValue.getValue());
   }

   @Override
   public Character asCharacter()
   {
      return ((Character) annotationValue.getValue());
   }

   @Override
   public Float asFloat()
   {
      return (Float) annotationValue.getValue();
   }

   @Override
   public Double asDouble()
   {
      return ((Double) annotationValue.getValue());
   }

   @Override
   public Shadow asType()
   {
      return MirrorAdapter.getShadow(context, (TypeMirror) annotationValue.getValue());
   }

   @Override
   public EnumConstant asEnumConstant()
   {
      return MirrorAdapter.getShadow(context, (Element) annotationValue.getValue());
   }

   @Override
   public AnnotationUsage asAnnotationUsage()
   {
      return AnnotationUsageImpl.from(context, (AnnotationMirror) annotationValue.getValue());
   }

   @Override
   public List<AnnotationValue> asListOfValues()
   {
      //noinspection unchecked
      return ((Collection<javax.lang.model.element.AnnotationValue>) annotationValue.getValue())
            .stream()
            .map(annotationValue1 -> new AnnotationValueImpl(context, annotationValue1, isDefaultValue()))
            .map(AnnotationValue.class::cast)
            .toList();
   }


   @Override
   public boolean isDefaultValue()
   {
      return defaultValue;
   }

   @Override
   public void consume(AnnotationValueConsumer consumer)
   {
      Object value = annotationValue.getValue();

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
      else if (value instanceof TypeMirror)
      {
         consumer.type(asType());
      }
      else if (value instanceof Element)
      {
         consumer.enumConstant(asEnumConstant());
      }
      else if (value instanceof AnnotationMirror)
      {
         consumer.annotationUsage(asAnnotationUsage());
      }
      else if (value instanceof Collection)
      {
         consumer.values(asListOfValues());
      }
   }

   @Override
   public <T> T map(AnnotationValueMapper<T> mapper)
   {
      Object value = annotationValue.getValue();

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
      if (value instanceof TypeMirror)
      {
         return mapper.type(asType());
      }
      if (value instanceof Element)
      {
         return mapper.enumConstant(asEnumConstant());
      }
      if (value instanceof AnnotationMirror)
      {
         return mapper.annotationUsage(asAnnotationUsage());
      }
      if (value instanceof Collection)
      {
         return mapper.values(asListOfValues());
      }
      throw new IllegalStateException();
   }

   public javax.lang.model.element.AnnotationValue getAnnotationValue()
   {
      return annotationValue;
   }

   @Override
   public boolean equals(Object other)
   {
      if (this == other)
      {
         return true;
      }
      if (!(other instanceof AnnotationValue otherAnnotationValue))
      {
         return false;
      }
      return isDefaultValue() == otherAnnotationValue.isDefaultValue() &&
             Objects.equals(getValue(), otherAnnotationValue.getValue());
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getAnnotationValue(), isDefaultValue());
   }

   @Override
   public String toString()
   {
      return annotationValue.toString();
   }
}

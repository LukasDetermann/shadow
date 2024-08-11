package io.determann.shadow.internal.lang_model.annotationvalue;

import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.lang_model.shadow.AnnotationValueLangModel;
import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.AnnotationValue;
import io.determann.shadow.api.shadow.structure.EnumConstant;
import io.determann.shadow.api.shadow.type.Shadow;
import io.determann.shadow.implementation.support.api.shadow.AnnotationValueSupport;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import java.util.Collection;
import java.util.List;

import static io.determann.shadow.internal.lang_model.LangModelProvider.IMPLEMENTATION_NAME;

public abstract class AnnotationValueImpl
{
   protected final LangModelContext context;
   private final boolean defaultValue;
   private final javax.lang.model.element.AnnotationValue annotationValue;

   public static AnnotationValueLangModel create(LangModelContext context,
                                                 javax.lang.model.element.AnnotationValue annotationValue,
                                                 boolean defaultValue)
   {
      Object value = annotationValue.getValue();

      if (value instanceof String)
      {
         return new StringValueImpl(context, annotationValue, defaultValue);
      }
      if (value instanceof Boolean)
      {
         return new BooleanValueImpl(context, annotationValue, defaultValue);
      }
      if (value instanceof Byte)
      {
         return new ByteValueImpl(context, annotationValue, defaultValue);
      }
      if (value instanceof Short)
      {
         return new ShortValueImpl(context, annotationValue, defaultValue);
      }
      if (value instanceof Integer)
      {
         return new IntegerValueImpl(context, annotationValue, defaultValue);
      }
      if (value instanceof Long)
      {
         return new LongValueImpl(context, annotationValue, defaultValue);
      }
      if (value instanceof Character)
      {
         return new CharacterValueImpl(context, annotationValue, defaultValue);
      }
      if (value instanceof Float)
      {
         return new FloatValueImpl(context, annotationValue, defaultValue);
      }
      if (value instanceof Double)
      {
         return new DoubleValueImpl(context, annotationValue, defaultValue);
      }
      if (value instanceof TypeMirror)
      {
         return new TypeValueImpl(context, annotationValue, defaultValue);
      }
      if (value instanceof Element)
      {
         return new EnumConstantValueImpl(context, annotationValue, defaultValue);
      }
      if (value instanceof AnnotationMirror)
      {
         return new AnnotationUsageValueImpl(context, annotationValue, defaultValue);
      }
      if (value instanceof Collection)
      {
         return new ValuesImpl(context, annotationValue, defaultValue);
      }
      throw new IllegalStateException();
   }

   private static class StringValueImpl extends AnnotationValueImpl implements AnnotationValueLangModel.StringValue
   {
      private final javax.lang.model.element.AnnotationValue annotationValue;

      StringValueImpl(LangModelContext context, javax.lang.model.element.AnnotationValue annotationValue, boolean defaultValue)
      {
         super(context, defaultValue, annotationValue);
         this.annotationValue = annotationValue;
      }

      @Override
      public String getValue()
      {
         return ((String) annotationValue.getValue());
      }
   }

   private static class BooleanValueImpl extends AnnotationValueImpl implements AnnotationValueLangModel.BooleanValue
   {
      private final javax.lang.model.element.AnnotationValue annotationValue;

      BooleanValueImpl(LangModelContext context, javax.lang.model.element.AnnotationValue annotationValue, boolean defaultValue)
      {
         super(context, defaultValue, annotationValue);
         this.annotationValue = annotationValue;
      }

      @Override
      public Boolean getValue()
      {
         return ((Boolean) annotationValue.getValue());
      }
   }

   private static class ByteValueImpl extends AnnotationValueImpl implements AnnotationValueLangModel.ByteValue
   {
      private final javax.lang.model.element.AnnotationValue annotationValue;

      ByteValueImpl(LangModelContext context, javax.lang.model.element.AnnotationValue annotationValue, boolean defaultValue)
      {
         super(context, defaultValue, annotationValue);
         this.annotationValue = annotationValue;
      }

      @Override
      public Byte getValue()
      {
         return ((Byte) annotationValue.getValue());
      }
   }

   private static class ShortValueImpl extends AnnotationValueImpl implements AnnotationValueLangModel.ShortValue
   {
      private final javax.lang.model.element.AnnotationValue annotationValue;

      ShortValueImpl(LangModelContext context, javax.lang.model.element.AnnotationValue annotationValue, boolean defaultValue)
      {
         super(context, defaultValue, annotationValue);
         this.annotationValue = annotationValue;
      }

      @Override
      public Short getValue()
      {
         return ((Short) annotationValue.getValue());
      }
   }

   private static class IntegerValueImpl extends AnnotationValueImpl implements AnnotationValueLangModel.IntegerValue
   {
      private final javax.lang.model.element.AnnotationValue annotationValue;

      IntegerValueImpl(LangModelContext context, javax.lang.model.element.AnnotationValue annotationValue, boolean defaultValue)
      {
         super(context, defaultValue, annotationValue);
         this.annotationValue = annotationValue;
      }

      @Override
      public Integer getValue()
      {
         return ((Integer) annotationValue.getValue());
      }
   }

   private static class LongValueImpl extends AnnotationValueImpl implements AnnotationValueLangModel.LongValue
   {
      private final javax.lang.model.element.AnnotationValue annotationValue;

      LongValueImpl(LangModelContext context, javax.lang.model.element.AnnotationValue annotationValue, boolean defaultValue)
      {
         super(context, defaultValue, annotationValue);
         this.annotationValue = annotationValue;
      }

      @Override
      public Long getValue()
      {
         return ((Long) annotationValue.getValue());
      }
   }

   private static class CharacterValueImpl extends AnnotationValueImpl implements AnnotationValueLangModel.CharacterValue
   {
      private final javax.lang.model.element.AnnotationValue annotationValue;

      CharacterValueImpl(LangModelContext context, javax.lang.model.element.AnnotationValue annotationValue, boolean defaultValue)
      {
         super(context, defaultValue, annotationValue);
         this.annotationValue = annotationValue;
      }

      @Override
      public Character getValue()
      {
         return ((Character) annotationValue.getValue());
      }
   }

   private static class FloatValueImpl extends AnnotationValueImpl implements AnnotationValueLangModel.FloatValue
   {
      private final javax.lang.model.element.AnnotationValue annotationValue;

      FloatValueImpl(LangModelContext context, javax.lang.model.element.AnnotationValue annotationValue, boolean defaultValue)
      {
         super(context, defaultValue, annotationValue);
         this.annotationValue = annotationValue;
      }

      @Override
      public Float getValue()
      {
         return ((Float) annotationValue.getValue());
      }
   }

   private static class DoubleValueImpl extends AnnotationValueImpl implements AnnotationValueLangModel.DoubleValue
   {
      private final javax.lang.model.element.AnnotationValue annotationValue;

      DoubleValueImpl(LangModelContext context, javax.lang.model.element.AnnotationValue annotationValue, boolean defaultValue)
      {
         super(context, defaultValue, annotationValue);
         this.annotationValue = annotationValue;
      }

      @Override
      public Double getValue()
      {
         return ((Double) annotationValue.getValue());
      }
   }

   private static class TypeValueImpl extends AnnotationValueImpl implements AnnotationValueLangModel.TypeValue
   {
      private final javax.lang.model.element.AnnotationValue annotationValue;

      TypeValueImpl(LangModelContext context, javax.lang.model.element.AnnotationValue annotationValue, boolean defaultValue)
      {
         super(context, defaultValue, annotationValue);
         this.annotationValue = annotationValue;
      }

      @Override
      public Shadow getValue()
      {
         return LangModelAdapter.generalize(context, (TypeMirror) annotationValue.getValue());
      }
   }

   private static class EnumConstantValueImpl extends AnnotationValueImpl implements AnnotationValueLangModel.EnumConstantValue
   {
      private final javax.lang.model.element.AnnotationValue annotationValue;

      EnumConstantValueImpl(LangModelContext context, javax.lang.model.element.AnnotationValue annotationValue, boolean defaultValue)
      {
         super(context, defaultValue, annotationValue);
         this.annotationValue = annotationValue;
      }

      @Override
      public EnumConstant getValue()
      {
         return LangModelAdapter.generalize(context, (Element) annotationValue.getValue());
      }
   }

   private static class AnnotationUsageValueImpl extends AnnotationValueImpl implements AnnotationValueLangModel.AnnotationUsageValue
   {
      private final javax.lang.model.element.AnnotationValue annotationValue;

      AnnotationUsageValueImpl(LangModelContext context, javax.lang.model.element.AnnotationValue annotationValue, boolean defaultValue)
      {
         super(context, defaultValue, annotationValue);
         this.annotationValue = annotationValue;
      }

      @Override
      public AnnotationUsage getValue()
      {
         return AnnotationUsageImpl.from(context, (AnnotationMirror) annotationValue.getValue());
      }
   }

   private static class ValuesImpl extends AnnotationValueImpl implements AnnotationValueLangModel.Values
   {
      private final javax.lang.model.element.AnnotationValue annotationValue;

      ValuesImpl(LangModelContext context, javax.lang.model.element.AnnotationValue annotationValue, boolean defaultValue)
      {
         super(context, defaultValue, annotationValue);
         this.annotationValue = annotationValue;
      }

      @Override
      public List<AnnotationValueLangModel> getValue()
      {
         //noinspection unchecked
         return ((Collection<javax.lang.model.element.AnnotationValue>) annotationValue.getValue())
               .stream()
               .map(annotationValue1 -> create(
                     context,
                     annotationValue1,
                     isDefault()))
               .map(AnnotationValueLangModel.class::cast)
               .toList();
      }
   }

   private AnnotationValueImpl(LangModelContext context, boolean defaultValue, javax.lang.model.element.AnnotationValue annotationValue)
   {
      this.context = context;
      this.defaultValue = defaultValue;
      this.annotationValue = annotationValue;
   }

   public boolean isDefault()
   {
      return defaultValue;
   }

   public javax.lang.model.element.AnnotationValue getAnnotationValue()
   {
      return annotationValue;
   }

   public abstract Object getValue();

   private <VALUE extends AnnotationValue, T> T getSave(Class<VALUE> valueClass, Class<T> tClass)
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
      return AnnotationValueSupport.equals((AnnotationValue) this, other);
   }

   @Override
   public int hashCode()
   {
      return AnnotationValueSupport.hashCode(((AnnotationValue) this));
   }

   @Override
   public String toString()
   {
      return AnnotationValueSupport.toString((AnnotationValue) this);
   }

   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
   }
}

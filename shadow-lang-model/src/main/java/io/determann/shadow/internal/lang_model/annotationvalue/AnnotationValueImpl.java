package io.determann.shadow.internal.lang_model.annotationvalue;

import io.determann.shadow.api.Implementation;
import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.adapter.LM_Adapters;
import io.determann.shadow.api.lang_model.shadow.LM_AnnotationUsage;
import io.determann.shadow.api.lang_model.shadow.LM_AnnotationValue;
import io.determann.shadow.api.lang_model.shadow.structure.LM_EnumConstant;
import io.determann.shadow.api.lang_model.shadow.type.LM_Type;
import io.determann.shadow.api.shadow.C_AnnotationValue;
import io.determann.shadow.implementation.support.api.shadow.AnnotationValueSupport;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.Collection;
import java.util.List;

public abstract class AnnotationValueImpl
{
   protected final LM_Context context;
   private final boolean defaultValue;
   private final AnnotationValue annotationValue;

   public static LM_AnnotationValue create(LM_Context context,
                                           AnnotationValue annotationValue,
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

   private static class StringValueImpl extends AnnotationValueImpl implements LM_AnnotationValue.StringValue
   {
      private final AnnotationValue annotationValue;

      StringValueImpl(LM_Context context, AnnotationValue annotationValue, boolean defaultValue)
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

   private static class BooleanValueImpl extends AnnotationValueImpl implements LM_AnnotationValue.BooleanValue
   {
      private final AnnotationValue annotationValue;

      BooleanValueImpl(LM_Context context, AnnotationValue annotationValue, boolean defaultValue)
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

   private static class ByteValueImpl extends AnnotationValueImpl implements LM_AnnotationValue.ByteValue
   {
      private final AnnotationValue annotationValue;

      ByteValueImpl(LM_Context context, AnnotationValue annotationValue, boolean defaultValue)
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

   private static class ShortValueImpl extends AnnotationValueImpl implements LM_AnnotationValue.ShortValue
   {
      private final AnnotationValue annotationValue;

      ShortValueImpl(LM_Context context, AnnotationValue annotationValue, boolean defaultValue)
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

   private static class IntegerValueImpl extends AnnotationValueImpl implements LM_AnnotationValue.IntegerValue
   {
      private final AnnotationValue annotationValue;

      IntegerValueImpl(LM_Context context, AnnotationValue annotationValue, boolean defaultValue)
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

   private static class LongValueImpl extends AnnotationValueImpl implements LM_AnnotationValue.LongValue
   {
      private final AnnotationValue annotationValue;

      LongValueImpl(LM_Context context, AnnotationValue annotationValue, boolean defaultValue)
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

   private static class CharacterValueImpl extends AnnotationValueImpl implements LM_AnnotationValue.CharacterValue
   {
      private final AnnotationValue annotationValue;

      CharacterValueImpl(LM_Context context, AnnotationValue annotationValue, boolean defaultValue)
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

   private static class FloatValueImpl extends AnnotationValueImpl implements LM_AnnotationValue.FloatValue
   {
      private final AnnotationValue annotationValue;

      FloatValueImpl(LM_Context context, AnnotationValue annotationValue, boolean defaultValue)
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

   private static class DoubleValueImpl extends AnnotationValueImpl implements LM_AnnotationValue.DoubleValue
   {
      private final AnnotationValue annotationValue;

      DoubleValueImpl(LM_Context context, AnnotationValue annotationValue, boolean defaultValue)
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

   private static class TypeValueImpl extends AnnotationValueImpl implements LM_AnnotationValue.TypeValue
   {
      private final AnnotationValue annotationValue;

      TypeValueImpl(LM_Context context, AnnotationValue annotationValue, boolean defaultValue)
      {
         super(context, defaultValue, annotationValue);
         this.annotationValue = annotationValue;
      }

      @Override
      public LM_Type getValue()
      {
         return LM_Adapters.adapt(context, (TypeMirror) annotationValue.getValue());
      }
   }

   private static class EnumConstantValueImpl extends AnnotationValueImpl implements LM_AnnotationValue.EnumConstantValue
   {
      private final AnnotationValue annotationValue;

      EnumConstantValueImpl(LM_Context context, AnnotationValue annotationValue, boolean defaultValue)
      {
         super(context, defaultValue, annotationValue);
         this.annotationValue = annotationValue;
      }

      @Override
      public LM_EnumConstant getValue()
      {
         return ((LM_EnumConstant) LM_Adapters.adapt(context, (VariableElement) annotationValue.getValue()));
      }
   }

   private static class AnnotationUsageValueImpl extends AnnotationValueImpl implements LM_AnnotationValue.AnnotationUsageValue
   {
      private final AnnotationValue annotationValue;

      AnnotationUsageValueImpl(LM_Context context, AnnotationValue annotationValue, boolean defaultValue)
      {
         super(context, defaultValue, annotationValue);
         this.annotationValue = annotationValue;
      }

      @Override
      public LM_AnnotationUsage getValue()
      {
         return AnnotationUsageImpl.from(context, (AnnotationMirror) annotationValue.getValue());
      }
   }

   private static class ValuesImpl extends AnnotationValueImpl implements LM_AnnotationValue.Values
   {
      private final AnnotationValue annotationValue;

      ValuesImpl(LM_Context context, AnnotationValue annotationValue, boolean defaultValue)
      {
         super(context, defaultValue, annotationValue);
         this.annotationValue = annotationValue;
      }

      @Override
      public List<LM_AnnotationValue> getValue()
      {
         //noinspection unchecked
         return ((Collection<AnnotationValue>) annotationValue.getValue())
               .stream()
               .map(annotationValue1 -> create(
                     context,
                     annotationValue1,
                     isDefault()))
               .map(LM_AnnotationValue.class::cast)
               .toList();
      }
   }

   private AnnotationValueImpl(LM_Context context, boolean defaultValue, AnnotationValue annotationValue)
   {
      this.context = context;
      this.defaultValue = defaultValue;
      this.annotationValue = annotationValue;
   }

   public boolean isDefault()
   {
      return defaultValue;
   }

   public AnnotationValue getAnnotationValue()
   {
      return annotationValue;
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

   public Implementation getImplementation()
   {
      return context.getImplementation();
   }
}

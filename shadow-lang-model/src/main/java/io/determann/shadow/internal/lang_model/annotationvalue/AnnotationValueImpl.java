package io.determann.shadow.internal.lang_model.annotationvalue;

import io.determann.shadow.api.C;
import io.determann.shadow.api.lang_model.LM;
import io.determann.shadow.api.lang_model.adapter.Adapters;
import io.determann.shadow.api.query.Implementation;
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
   protected final LM.Context context;
   private final boolean defaultValue;
   private final AnnotationValue annotationValue;

   public static LM.AnnotationValue create(LM.Context context,
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

   private static class StringValueImpl extends AnnotationValueImpl implements LM.AnnotationValue.StringValue
   {
      private final AnnotationValue annotationValue;

      StringValueImpl(LM.Context context, AnnotationValue annotationValue, boolean defaultValue)
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

   private static class BooleanValueImpl extends AnnotationValueImpl implements LM.AnnotationValue.BooleanValue
   {
      private final AnnotationValue annotationValue;

      BooleanValueImpl(LM.Context context, AnnotationValue annotationValue, boolean defaultValue)
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

   private static class ByteValueImpl extends AnnotationValueImpl implements LM.AnnotationValue.ByteValue
   {
      private final AnnotationValue annotationValue;

      ByteValueImpl(LM.Context context, AnnotationValue annotationValue, boolean defaultValue)
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

   private static class ShortValueImpl extends AnnotationValueImpl implements LM.AnnotationValue.ShortValue
   {
      private final AnnotationValue annotationValue;

      ShortValueImpl(LM.Context context, AnnotationValue annotationValue, boolean defaultValue)
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

   private static class IntegerValueImpl extends AnnotationValueImpl implements LM.AnnotationValue.IntegerValue
   {
      private final AnnotationValue annotationValue;

      IntegerValueImpl(LM.Context context, AnnotationValue annotationValue, boolean defaultValue)
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

   private static class LongValueImpl extends AnnotationValueImpl implements LM.AnnotationValue.LongValue
   {
      private final AnnotationValue annotationValue;

      LongValueImpl(LM.Context context, AnnotationValue annotationValue, boolean defaultValue)
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

   private static class CharacterValueImpl extends AnnotationValueImpl implements LM.AnnotationValue.CharacterValue
   {
      private final AnnotationValue annotationValue;

      CharacterValueImpl(LM.Context context, AnnotationValue annotationValue, boolean defaultValue)
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

   private static class FloatValueImpl extends AnnotationValueImpl implements LM.AnnotationValue.FloatValue
   {
      private final AnnotationValue annotationValue;

      FloatValueImpl(LM.Context context, AnnotationValue annotationValue, boolean defaultValue)
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

   private static class DoubleValueImpl extends AnnotationValueImpl implements LM.AnnotationValue.DoubleValue
   {
      private final AnnotationValue annotationValue;

      DoubleValueImpl(LM.Context context, AnnotationValue annotationValue, boolean defaultValue)
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

   private static class TypeValueImpl extends AnnotationValueImpl implements LM.AnnotationValue.TypeValue
   {
      private final AnnotationValue annotationValue;

      TypeValueImpl(LM.Context context, AnnotationValue annotationValue, boolean defaultValue)
      {
         super(context, defaultValue, annotationValue);
         this.annotationValue = annotationValue;
      }

      @Override
      public LM.Type getValue()
      {
         return Adapters.adapt(context, (TypeMirror) annotationValue.getValue());
      }
   }

   private static class EnumConstantValueImpl extends AnnotationValueImpl implements LM.AnnotationValue.EnumConstantValue
   {
      private final AnnotationValue annotationValue;

      EnumConstantValueImpl(LM.Context context, AnnotationValue annotationValue, boolean defaultValue)
      {
         super(context, defaultValue, annotationValue);
         this.annotationValue = annotationValue;
      }

      @Override
      public LM.EnumConstant getValue()
      {
         return ((LM.EnumConstant) Adapters.adapt(context, (VariableElement) annotationValue.getValue()));
      }
   }

   private static class AnnotationUsageValueImpl extends AnnotationValueImpl implements LM.AnnotationValue.AnnotationUsageValue
   {
      private final AnnotationValue annotationValue;

      AnnotationUsageValueImpl(LM.Context context, AnnotationValue annotationValue, boolean defaultValue)
      {
         super(context, defaultValue, annotationValue);
         this.annotationValue = annotationValue;
      }

      @Override
      public LM.AnnotationUsage getValue()
      {
         return AnnotationUsageImpl.from(context, (AnnotationMirror) annotationValue.getValue());
      }
   }

   private static class ValuesImpl extends AnnotationValueImpl implements LM.AnnotationValue.Values
   {
      private final AnnotationValue annotationValue;

      ValuesImpl(LM.Context context, AnnotationValue annotationValue, boolean defaultValue)
      {
         super(context, defaultValue, annotationValue);
         this.annotationValue = annotationValue;
      }

      @Override
      public List<LM.AnnotationValue> getValue()
      {
         //noinspection unchecked
         return ((Collection<AnnotationValue>) annotationValue.getValue())
               .stream()
               .map(annotationValue1 -> create(
                     context,
                     annotationValue1,
                     isDefault()))
               .map(LM.AnnotationValue.class::cast)
               .toList();
      }
   }

   private AnnotationValueImpl(LM.Context context, boolean defaultValue, AnnotationValue annotationValue)
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

   private <VALUE extends C.AnnotationValue, T> T getSave(Class<VALUE> valueClass, Class<T> tClass)
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
      return AnnotationValueSupport.equals((C.AnnotationValue) this, other);
   }

   @Override
   public int hashCode()
   {
      return AnnotationValueSupport.hashCode(((C.AnnotationValue) this));
   }

   @Override
   public String toString()
   {
      return AnnotationValueSupport.toString((C.AnnotationValue) this);
   }

   public Implementation getImplementation()
   {
      return context.getImplementation();
   }
}

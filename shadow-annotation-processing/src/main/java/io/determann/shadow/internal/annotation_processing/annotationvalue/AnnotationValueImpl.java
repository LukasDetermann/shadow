package io.determann.shadow.internal.annotation_processing.annotationvalue;

import io.determann.shadow.api.C;
import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.adapter.Adapters;
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
   protected final Ap.Context context;
   private final boolean defaultValue;
   private final AnnotationValue annotationValue;

   public static Ap.AnnotationValue create(Ap.Context context,
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
         return new EnumValueImpl(context, annotationValue, defaultValue);
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

   private static class StringValueImpl extends AnnotationValueImpl implements Ap.AnnotationValue.StringValue
   {
      private final AnnotationValue annotationValue;

      StringValueImpl(Ap.Context context, AnnotationValue annotationValue, boolean defaultValue)
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

   private static class BooleanValueImpl extends AnnotationValueImpl implements Ap.AnnotationValue.BooleanValue
   {
      private final AnnotationValue annotationValue;

      BooleanValueImpl(Ap.Context context, AnnotationValue annotationValue, boolean defaultValue)
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

   private static class ByteValueImpl extends AnnotationValueImpl implements Ap.AnnotationValue.ByteValue
   {
      private final AnnotationValue annotationValue;

      ByteValueImpl(Ap.Context context, AnnotationValue annotationValue, boolean defaultValue)
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

   private static class ShortValueImpl extends AnnotationValueImpl implements Ap.AnnotationValue.ShortValue
   {
      private final AnnotationValue annotationValue;

      ShortValueImpl(Ap.Context context, AnnotationValue annotationValue, boolean defaultValue)
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

   private static class IntegerValueImpl extends AnnotationValueImpl implements Ap.AnnotationValue.IntegerValue
   {
      private final AnnotationValue annotationValue;

      IntegerValueImpl(Ap.Context context, AnnotationValue annotationValue, boolean defaultValue)
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

   private static class LongValueImpl extends AnnotationValueImpl implements Ap.AnnotationValue.LongValue
   {
      private final AnnotationValue annotationValue;

      LongValueImpl(Ap.Context context, AnnotationValue annotationValue, boolean defaultValue)
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

   private static class CharacterValueImpl extends AnnotationValueImpl implements Ap.AnnotationValue.CharacterValue
   {
      private final AnnotationValue annotationValue;

      CharacterValueImpl(Ap.Context context, AnnotationValue annotationValue, boolean defaultValue)
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

   private static class FloatValueImpl extends AnnotationValueImpl implements Ap.AnnotationValue.FloatValue
   {
      private final AnnotationValue annotationValue;

      FloatValueImpl(Ap.Context context, AnnotationValue annotationValue, boolean defaultValue)
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

   private static class DoubleValueImpl extends AnnotationValueImpl implements Ap.AnnotationValue.DoubleValue
   {
      private final AnnotationValue annotationValue;

      DoubleValueImpl(Ap.Context context, AnnotationValue annotationValue, boolean defaultValue)
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

   private static class TypeValueImpl extends AnnotationValueImpl implements Ap.AnnotationValue.TypeValue
   {
      private final AnnotationValue annotationValue;

      TypeValueImpl(Ap.Context context, AnnotationValue annotationValue, boolean defaultValue)
      {
         super(context, defaultValue, annotationValue);
         this.annotationValue = annotationValue;
      }

      @Override
      public Ap.Type getValue()
      {
         return Adapters.adapt(context, (TypeMirror) annotationValue.getValue());
      }
   }

   private static class EnumValueImpl
         extends AnnotationValueImpl implements Ap.AnnotationValue.EnumValue
   {
      private final AnnotationValue annotationValue;

      EnumValueImpl(Ap.Context context, AnnotationValue annotationValue, boolean defaultValue)
      {
         super(context, defaultValue, annotationValue);
         this.annotationValue = annotationValue;
      }

      @Override
      public Ap.EnumConstant getValue()
      {
         return ((Ap.EnumConstant) Adapters.adapt(context, (VariableElement) annotationValue.getValue()));
      }
   }

   private static class AnnotationUsageValueImpl extends AnnotationValueImpl implements Ap.AnnotationValue.AnnotationUsageValue
   {
      private final AnnotationValue annotationValue;

      AnnotationUsageValueImpl(Ap.Context context, AnnotationValue annotationValue, boolean defaultValue)
      {
         super(context, defaultValue, annotationValue);
         this.annotationValue = annotationValue;
      }

      @Override
      public Ap.AnnotationUsage getValue()
      {
         return AnnotationUsageImpl.from(context, (AnnotationMirror) annotationValue.getValue());
      }
   }

   private static class ValuesImpl extends AnnotationValueImpl implements Ap.AnnotationValue.Values
   {
      private final AnnotationValue annotationValue;

      ValuesImpl(Ap.Context context, AnnotationValue annotationValue, boolean defaultValue)
      {
         super(context, defaultValue, annotationValue);
         this.annotationValue = annotationValue;
      }

      @Override
      public List<Ap.AnnotationValue> getValue()
      {
         //noinspection unchecked
         return ((Collection<AnnotationValue>) annotationValue.getValue())
               .stream()
               .map(annotationValue1 -> create(
                     context,
                     annotationValue1,
                     isDefault()))
               .toList();
      }
   }

   private AnnotationValueImpl(Ap.Context context, boolean defaultValue, AnnotationValue annotationValue)
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

package io.determann.shadow.impl.shadow.wraper;

import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.EnumConstant;
import io.determann.shadow.api.shadow.Shadow;
import io.determann.shadow.api.wrapper.AnnotationValueTypeChooser;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public class AnnotationValueTypeChooserImpl implements AnnotationValueTypeChooser
{
   private final ShadowApi shadowApi;
   private final AnnotationValue annotationValue;
   private final boolean defaultValue;

   AnnotationValueTypeChooserImpl(ShadowApi shadowApi, AnnotationValue annotationValue, boolean defaultValue)
   {
      this.shadowApi = shadowApi;
      this.annotationValue = annotationValue;
      this.defaultValue = defaultValue;
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
   public Shadow<TypeMirror> asType()
   {
      return shadowApi.getShadowFactory().shadowFromType((TypeMirror) annotationValue.getValue());
   }

   @Override
   public EnumConstant asEnumConstant()
   {
      return shadowApi.getShadowFactory().shadowFromElement((Element) annotationValue.getValue());
   }

   @Override
   public AnnotationUsage asAnnotationUsage()
   {
      return AnnotationUsageImpl.from(shadowApi, (AnnotationMirror) annotationValue.getValue());
   }

   @Override
   public List<AnnotationValueTypeChooser> asListOfValues()
   {
      //noinspection unchecked
      return ((Collection<AnnotationValue>) annotationValue.getValue())
            .stream()
            .map(annotationValue1 -> new AnnotationValueTypeChooserImpl(shadowApi, annotationValue1, isDefaultValue()))
            .map(AnnotationValueTypeChooser.class::cast)
            .collect(collectingAndThen(toList(), Collections::unmodifiableList));
   }


   @Override
   public boolean isDefaultValue()
   {
      return defaultValue;
   }

   @Override
   public AnnotationValue getAnnotationValue()
   {
      return annotationValue;
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o)
      {
         return true;
      }
      if (o instanceof AnnotationValueTypeChooserImpl)
      {
         AnnotationValueTypeChooserImpl that = ((AnnotationValueTypeChooserImpl) o);
         return isDefaultValue() == that.isDefaultValue() &&
                Objects.equals(getAnnotationValue(), that.getAnnotationValue());
      }
      return false;
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

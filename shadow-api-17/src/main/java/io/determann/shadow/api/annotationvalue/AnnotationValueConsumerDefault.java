package io.determann.shadow.api.annotationvalue;

import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.EnumConstant;
import io.determann.shadow.api.shadow.Shadow;

import javax.lang.model.type.TypeMirror;
import java.util.List;

public abstract class AnnotationValueConsumerDefault implements AnnotationValueConsumer
{
   @Override
   public void string(String value)
   {
   }

   @Override
   public void aBoolean(Boolean value)
   {
   }

   @Override
   public void aByte(Byte value)
   {
   }

   @Override
   public void aShort(Short value)
   {
   }

   @Override
   public void integer(Integer value)
   {
   }

   @Override
   public void aLong(Long value)
   {
   }

   @Override
   public void character(Character value)
   {
   }

   @Override
   public void aFloat(Float value)
   {
   }

   @Override
   public void aDouble(Double value)
   {
   }

   @Override
   public void type(Shadow<TypeMirror> value)
   {
   }

   @Override
   public void enumConstant(EnumConstant value)
   {
   }

   @Override
   public void annotationUsage(AnnotationUsage value)
   {
   }

   @Override
   public void values(List<AnnotationValueTypeChooser> values)
   {
   }
}

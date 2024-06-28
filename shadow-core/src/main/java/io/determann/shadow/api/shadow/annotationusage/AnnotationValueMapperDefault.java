package io.determann.shadow.api.shadow.annotationusage;

import io.determann.shadow.api.shadow.structure.EnumConstant;
import io.determann.shadow.api.shadow.type.Shadow;

import java.util.List;

public abstract class AnnotationValueMapperDefault<T> implements AnnotationValueMapper<T>
{
   @Override
   public T string(String value)
   {
      return null;
   }

   @Override
   public T aBoolean(Boolean value)
   {
      return null;
   }

   @Override
   public T aByte(Byte value)
   {
      return null;
   }

   @Override
   public T aShort(Short value)
   {
      return null;
   }

   @Override
   public T integer(Integer value)
   {
      return null;
   }

   @Override
   public T aLong(Long value)
   {
      return null;
   }

   @Override
   public T character(Character value)
   {
      return null;
   }

   @Override
   public T aFloat(Float value)
   {
      return null;
   }

   @Override
   public T aDouble(Double value)
   {
      return null;
   }

   @Override
   public T type(Shadow value)
   {
      return null;
   }

   @Override
   public T enumConstant(EnumConstant value)
   {
      return null;
   }

   @Override
   public T annotationUsage(AnnotationUsage value)
   {
      return null;
   }

   @Override
   public T values(List<AnnotationValue> values)
   {
      return null;
   }
}

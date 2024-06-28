package io.determann.shadow.api.shadow.annotationusage;

import io.determann.shadow.api.shadow.structure.EnumConstant;
import io.determann.shadow.api.shadow.type.Shadow;

import java.util.List;

public interface AnnotationValueConsumer
{
   void string(String value);

   void aBoolean(Boolean value);

   void aByte(Byte value);

   void aShort(Short value);

   void integer(Integer value);

   void aLong(Long value);

   void character(Character value);

   void aFloat(Float value);

   void aDouble(Double value);

   void type(Shadow value);

   void enumConstant(EnumConstant value);

   void annotationUsage(AnnotationUsage value);

   void values(List<AnnotationValue> values);
}

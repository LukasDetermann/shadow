package io.determann.shadow.api.annotationvalue;

import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.EnumConstant;
import io.determann.shadow.api.shadow.Shadow;

import javax.lang.model.type.TypeMirror;
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

   void type(Shadow<TypeMirror> value);

   void enumConstant(EnumConstant value);

   void annotationUsage(AnnotationUsage value);

   void values(List<AnnotationValueTypeChooser> values);
}

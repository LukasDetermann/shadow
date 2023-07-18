package io.determann.shadow.api.annotationvalue;

import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.EnumConstant;
import io.determann.shadow.api.shadow.Shadow;

import javax.lang.model.type.TypeMirror;
import java.util.List;

public interface AnnotationValueMapper<T>
{
   T string(String value);

   T aBoolean(Boolean value);

   T aByte(Byte value);

   T aShort(Short value);

   T integer(Integer value);

   T aLong(Long value);

   T character(Character value);

   T aFloat(Float value);

   T aDouble(Double value);

   T type(Shadow<TypeMirror> value);

   T enumConstant(EnumConstant value);

   T annotationUsage(AnnotationUsage value);

   T values(List<AnnotationValue> values);
}

package io.determann.shadow.api.annotationvalue;

import io.determann.shadow.api.metadata.JdkApi;
import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.EnumConstant;
import io.determann.shadow.api.shadow.Shadow;

import java.util.List;

public interface AnnotationValue
{
   void consume(AnnotationValueConsumer consumer);

   public <T> T map(AnnotationValueMapper<T> mapper);

   /**
    * is this the default value specified in the annotation?
    */
   boolean isDefaultValue();

   String asString();

   Boolean asBoolean();

   Byte asByte();

   Short asShort();

   Integer asInteger();

   Long asLong();

   Character asCharacter();

   Float asFloat();

   Double asDouble();

   Shadow asType();

   EnumConstant asEnumConstant();

   AnnotationUsage asAnnotationUsage();

   List<AnnotationValue> asListOfValues();

   @JdkApi
   javax.lang.model.element.AnnotationValue getAnnotationValue();

   /**
    * calls {@code toString} on the underlying value
    */
   String toString();
}

package io.determann.shadow.api.shadow.annotationusage;

import io.determann.shadow.api.shadow.structure.EnumConstant;
import io.determann.shadow.api.shadow.type.Shadow;

import java.util.List;

public interface AnnotationValue
{
   void consume(AnnotationValueConsumer consumer);

   public <T> T map(AnnotationValueMapper<T> mapper);

   /**
    * is this the default value specified in the annotation?
    */
   boolean isDefaultValue();

   Object getValue();

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
}

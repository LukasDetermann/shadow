package org.determann.shadow.api.wrapper;

import org.determann.shadow.api.metadata.JdkApi;
import org.determann.shadow.api.shadow.AnnotationUsage;
import org.determann.shadow.api.shadow.EnumConstant;
import org.determann.shadow.api.shadow.Shadow;
import org.jetbrains.annotations.UnmodifiableView;

import javax.lang.model.element.AnnotationValue;
import javax.lang.model.type.TypeMirror;
import java.util.List;

public interface AnnotationValueTypeChooser
{
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

   Shadow<TypeMirror> asType();

   EnumConstant asEnumConstant();

   AnnotationUsage asAnnotationUsage();

   @UnmodifiableView List<AnnotationValueTypeChooser> asListOfValues();

   @JdkApi
   AnnotationValue getAnnotationValue();
}

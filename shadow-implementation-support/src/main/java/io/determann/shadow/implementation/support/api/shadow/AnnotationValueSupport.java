package io.determann.shadow.implementation.support.api.shadow;

import io.determann.shadow.api.shadow.AnnotationValue;
import io.determann.shadow.implementation.support.internal.shadow.SupportSupport;

import static io.determann.shadow.api.shadow.Operations.ANNOTATION_VALUE_GET_VALUE;
import static io.determann.shadow.api.shadow.Operations.ANNOTATION_VALUE_IS_DEFAULT;

public class AnnotationValueSupport
{
   public static boolean equals(AnnotationValue annotationValue, Object other)
   {
      return SupportSupport.equals(annotationValue, AnnotationValue.class, other, ANNOTATION_VALUE_GET_VALUE, ANNOTATION_VALUE_IS_DEFAULT);
   }

   public static int hashCode(AnnotationValue annotationValue)
   {
      return SupportSupport.hashCode(annotationValue, ANNOTATION_VALUE_GET_VALUE, ANNOTATION_VALUE_IS_DEFAULT);
   }

   public static String toString(AnnotationValue annotationValue)
   {
      return SupportSupport.toString(annotationValue, AnnotationValue.class, ANNOTATION_VALUE_GET_VALUE, ANNOTATION_VALUE_IS_DEFAULT);
   }
}

package io.determann.shadow.implementation.support.api.shadow;

import io.determann.shadow.api.shadow.C_AnnotationValue;
import io.determann.shadow.implementation.support.internal.shadow.SupportSupport;

import static io.determann.shadow.api.Operations.ANNOTATION_VALUE_GET_VALUE;
import static io.determann.shadow.api.Operations.ANNOTATION_VALUE_IS_DEFAULT;

public class AnnotationValueSupport
{
   public static boolean equals(C_AnnotationValue annotationValue, Object other)
   {
      return SupportSupport.equals(annotationValue, C_AnnotationValue.class, other, ANNOTATION_VALUE_GET_VALUE, ANNOTATION_VALUE_IS_DEFAULT);
   }

   public static int hashCode(C_AnnotationValue annotationValue)
   {
      return SupportSupport.hashCode(annotationValue, ANNOTATION_VALUE_GET_VALUE, ANNOTATION_VALUE_IS_DEFAULT);
   }

   public static String toString(C_AnnotationValue annotationValue)
   {
      return SupportSupport.toString(annotationValue, C_AnnotationValue.class, ANNOTATION_VALUE_GET_VALUE, ANNOTATION_VALUE_IS_DEFAULT);
   }
}

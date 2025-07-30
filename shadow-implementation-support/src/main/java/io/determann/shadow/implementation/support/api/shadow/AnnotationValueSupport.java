package io.determann.shadow.implementation.support.api.shadow;

import io.determann.shadow.api.C;
import io.determann.shadow.implementation.support.internal.shadow.SupportSupport;

import static io.determann.shadow.api.query.Operations.ANNOTATION_VALUE_GET_VALUE;
import static io.determann.shadow.api.query.Operations.ANNOTATION_VALUE_IS_DEFAULT;

public class AnnotationValueSupport
{
   public static boolean equals(C.AnnotationValue annotationValue, Object other)
   {
      return SupportSupport.equals(annotationValue, C.AnnotationValue.class, other, ANNOTATION_VALUE_GET_VALUE, ANNOTATION_VALUE_IS_DEFAULT);
   }

   public static int hashCode(C.AnnotationValue annotationValue)
   {
      return SupportSupport.hashCode(annotationValue, ANNOTATION_VALUE_GET_VALUE, ANNOTATION_VALUE_IS_DEFAULT);
   }

   public static String toString(C.AnnotationValue annotationValue)
   {
      return SupportSupport.toString(annotationValue, C.AnnotationValue.class, ANNOTATION_VALUE_GET_VALUE, ANNOTATION_VALUE_IS_DEFAULT);
   }
}

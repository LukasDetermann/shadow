package io.determann.shadow.implementation.support.api.shadow.type;

import io.determann.shadow.api.shadow.type.Annotation;

public class AnnotationSupport
{
   public static boolean equals(Annotation annotation, Object other)
   {
      return DeclaredSupport.equals(annotation, other, Annotation.class);
   }

   public static int hashCode(Annotation annotation)
   {
      return DeclaredSupport.hashCode(annotation);
   }

   public static String toString(Annotation annotation)
   {
      return DeclaredSupport.toString(annotation, Annotation.class);
   }
}

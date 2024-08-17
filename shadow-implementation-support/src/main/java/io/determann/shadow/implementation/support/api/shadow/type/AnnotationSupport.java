package io.determann.shadow.implementation.support.api.shadow.type;

import io.determann.shadow.api.shadow.type.Annotation;
import io.determann.shadow.api.shadow.type.Shadow;
import io.determann.shadow.implementation.support.internal.shadow.SupportSupport;

import static io.determann.shadow.api.shadow.Operations.MODULE_ENCLOSED_GET_MODULE;
import static io.determann.shadow.api.shadow.Operations.QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME;

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

   public static boolean representsSameType(Annotation annotation, Shadow other)
   {
      return SupportSupport.representsSameType(annotation, Annotation.class, other,
                                               MODULE_ENCLOSED_GET_MODULE,
                                               //should be the binary name. this is close enough for most cases
                                               QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME);
   }
}

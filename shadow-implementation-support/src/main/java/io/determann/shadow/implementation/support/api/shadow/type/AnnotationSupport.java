package io.determann.shadow.implementation.support.api.shadow.type;

import io.determann.shadow.api.C;
import io.determann.shadow.implementation.support.internal.shadow.SupportSupport;

import static io.determann.shadow.api.query.Operations.MODULE_ENCLOSED_GET_MODULE;
import static io.determann.shadow.api.query.Operations.QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME;

public class AnnotationSupport
{
   public static boolean equals(C.Annotation annotation, Object other)
   {
      return DeclaredSupport.equals(annotation, other, C.Annotation.class);
   }

   public static int hashCode(C.Annotation annotation)
   {
      return DeclaredSupport.hashCode(annotation);
   }

   public static String toString(C.Annotation annotation)
   {
      return DeclaredSupport.toString(annotation, C.Annotation.class);
   }

   public static boolean representsSameType(C.Annotation annotation, C.Type other)
   {
      return SupportSupport.representsSameType(annotation, C.Annotation.class, other,
                                               MODULE_ENCLOSED_GET_MODULE,
                                               //should be the binary name. this is close enough for most cases
                                               QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME);
   }
}

package io.determann.shadow.implementation.support.api.shadow.type;

import io.determann.shadow.api.shadow.type.C_Annotation;
import io.determann.shadow.api.shadow.type.C_Type;
import io.determann.shadow.implementation.support.internal.shadow.SupportSupport;

import static io.determann.shadow.api.Operations.MODULE_ENCLOSED_GET_MODULE;
import static io.determann.shadow.api.Operations.QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME;

public class AnnotationSupport
{
   public static boolean equals(C_Annotation annotation, Object other)
   {
      return DeclaredSupport.equals(annotation, other, C_Annotation.class);
   }

   public static int hashCode(C_Annotation annotation)
   {
      return DeclaredSupport.hashCode(annotation);
   }

   public static String toString(C_Annotation annotation)
   {
      return DeclaredSupport.toString(annotation, C_Annotation.class);
   }

   public static boolean representsSameType(C_Annotation annotation, C_Type other)
   {
      return SupportSupport.representsSameType(annotation, C_Annotation.class, other,
                                               MODULE_ENCLOSED_GET_MODULE,
                                               //should be the binary name. this is close enough for most cases
                                               QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME);
   }
}

package io.determann.shadow.implementation.support.api.shadow.type;

import io.determann.shadow.api.C;
import io.determann.shadow.implementation.support.internal.shadow.SupportSupport;

import static io.determann.shadow.api.query.Operations.*;

public class ClassSupport
{
   public static boolean equals(C.Class aClass, Object other)
   {
      return DeclaredSupport.equals(aClass, other, C.Class.class);
   }

   public static int hashCode(C.Class aClass)
   {
      return DeclaredSupport.hashCode(aClass);
   }

   public static String toString(C.Class aClass)
   {
      return DeclaredSupport.toString(aClass, C.Class.class);
   }

   public static boolean representsSameType(C.Class aClass, C.Type other)
   {
      return SupportSupport.representsSameType(aClass, C.Class.class, other,
                                               MODULE_ENCLOSED_GET_MODULE,
                                               //should be the binary name. this is close enough for most cases
                                               QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME,
                                               CLASS_GET_GENERIC_USAGES);
   }
}

package io.determann.shadow.implementation.support.api.shadow.type;

import io.determann.shadow.api.shadow.type.Class;
import io.determann.shadow.api.shadow.type.Shadow;
import io.determann.shadow.implementation.support.internal.shadow.SupportSupport;

import static io.determann.shadow.api.shadow.Operations.*;

public class ClassSupport
{
   public static boolean equals(Class aClass, Object other)
   {
      return DeclaredSupport.equals(aClass, other, Class.class);
   }

   public static int hashCode(Class aClass)
   {
      return DeclaredSupport.hashCode(aClass);
   }

   public static String toString(Class aClass)
   {
      return DeclaredSupport.toString(aClass, Class.class);
   }

   public static boolean representsSameType(Class aClass, Shadow other)
   {
      return SupportSupport.representsSameType(aClass, Class.class, other,
                                               MODULE_ENCLOSED_GET_MODULE,
                                               //should be the binary name. this is close enough for most cases
                                               QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME,
                                               CLASS_GET_GENERIC_TYPES);
   }
}

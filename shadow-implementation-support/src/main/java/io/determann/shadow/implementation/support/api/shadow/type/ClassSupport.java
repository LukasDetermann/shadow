package io.determann.shadow.implementation.support.api.shadow.type;

import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Shadow;
import io.determann.shadow.implementation.support.internal.shadow.SupportSupport;

import static io.determann.shadow.api.Operations.*;

public class ClassSupport
{
   public static boolean equals(C_Class aClass, Object other)
   {
      return DeclaredSupport.equals(aClass, other, C_Class.class);
   }

   public static int hashCode(C_Class aClass)
   {
      return DeclaredSupport.hashCode(aClass);
   }

   public static String toString(C_Class aClass)
   {
      return DeclaredSupport.toString(aClass, C_Class.class);
   }

   public static boolean representsSameType(C_Class aClass, C_Shadow other)
   {
      return SupportSupport.representsSameType(aClass, C_Class.class, other,
                                               MODULE_ENCLOSED_GET_MODULE,
                                               //should be the binary name. this is close enough for most cases
                                               QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME,
                                               CLASS_GET_GENERIC_TYPES);
   }
}

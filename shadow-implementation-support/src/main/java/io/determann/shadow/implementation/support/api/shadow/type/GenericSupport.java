package io.determann.shadow.implementation.support.api.shadow.type;

import io.determann.shadow.api.shadow.type.C_Generic;
import io.determann.shadow.implementation.support.internal.shadow.SupportSupport;

import static io.determann.shadow.api.Operations.*;

public class GenericSupport
{
   public static boolean equals(C_Generic generic, Object other)
   {
      return SupportSupport.equals(generic,
                                   C_Generic.class,
                                   other,
                                   NAMEABLE_GET_NAME,
                                   GENERIC_GET_EXTENDS,
                                   GENERIC_GET_SUPER);
   }

   public static int hashCode(C_Generic generic)
   {
      return SupportSupport.hashCode(generic, NAMEABLE_GET_NAME, GENERIC_GET_EXTENDS, GENERIC_GET_SUPER);
   }

   public static String toString(C_Generic generic)
   {
      return SupportSupport.toString(generic, C_Generic.class, NAMEABLE_GET_NAME, GENERIC_GET_EXTENDS, GENERIC_GET_SUPER);
   }
}

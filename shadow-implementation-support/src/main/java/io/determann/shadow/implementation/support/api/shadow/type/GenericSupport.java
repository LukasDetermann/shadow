package io.determann.shadow.implementation.support.api.shadow.type;

import io.determann.shadow.api.shadow.type.Generic;
import io.determann.shadow.implementation.support.internal.shadow.SupportSupport;

import static io.determann.shadow.api.shadow.Operations.*;

public class GenericSupport
{
   public static boolean equals(Generic generic, Object other)
   {
      return SupportSupport.equals(generic,
                                   Generic.class,
                                   other,
                                   NAMEABLE_GET_NAME,
                                   GENERIC_GET_EXTENDS,
                                   GENERIC_GET_SUPER);
   }

   public static int hashCode(Generic generic)
   {
      return SupportSupport.hashCode(generic, NAMEABLE_GET_NAME, GENERIC_GET_EXTENDS, GENERIC_GET_SUPER);
   }

   public static String toString(Generic generic)
   {
      return SupportSupport.toString(generic, Generic.class, NAMEABLE_GET_NAME, GENERIC_GET_EXTENDS, GENERIC_GET_SUPER);
   }
}

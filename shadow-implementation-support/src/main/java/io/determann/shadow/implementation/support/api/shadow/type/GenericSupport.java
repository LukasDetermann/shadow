package io.determann.shadow.implementation.support.api.shadow.type;

import io.determann.shadow.api.C;
import io.determann.shadow.implementation.support.internal.shadow.SupportSupport;

import static io.determann.shadow.api.query.Operations.GENERIC_GET_BOUNDS;
import static io.determann.shadow.api.query.Operations.NAMEABLE_GET_NAME;

public class GenericSupport
{
   public static boolean equals(C.Generic generic, Object other)
   {
      return SupportSupport.equals(generic,
                                   C.Generic.class,
                                   other,
                                   NAMEABLE_GET_NAME,
                                   GENERIC_GET_BOUNDS);
   }

   public static int hashCode(C.Generic generic)
   {
      return SupportSupport.hashCode(generic, NAMEABLE_GET_NAME, GENERIC_GET_BOUNDS);
   }

   public static String toString(C.Generic generic)
   {
      return SupportSupport.toString(generic, C.Generic.class, NAMEABLE_GET_NAME, GENERIC_GET_BOUNDS);
   }
}

package io.determann.shadow.implementation.support.api.shadow.type;

import io.determann.shadow.api.C;
import io.determann.shadow.implementation.support.internal.shadow.SupportSupport;

import static io.determann.shadow.api.query.Operations.WILDCARD_GET_EXTENDS;
import static io.determann.shadow.api.query.Operations.WILDCARD_GET_SUPER;

public class WildcardSupport
{
   public static boolean equals(C.Wildcard wildcard, Object other)
   {
      return SupportSupport.equals(wildcard,
                                   C.Wildcard.class,
                                   other,
                                   WILDCARD_GET_EXTENDS,
                                   WILDCARD_GET_SUPER);
   }

   public static int hashCode(C.Wildcard wildcard)
   {
      return SupportSupport.hashCode(wildcard, WILDCARD_GET_EXTENDS, WILDCARD_GET_SUPER);
   }

   public static String toString(C.Wildcard wildcard)
   {
      return SupportSupport.toString(wildcard, C.Wildcard.class, WILDCARD_GET_EXTENDS, WILDCARD_GET_SUPER);
   }
}

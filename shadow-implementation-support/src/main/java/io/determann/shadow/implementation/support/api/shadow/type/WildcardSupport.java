package io.determann.shadow.implementation.support.api.shadow.type;

import io.determann.shadow.api.shadow.type.Wildcard;
import io.determann.shadow.implementation.support.internal.shadow.SupportSupport;

import static io.determann.shadow.api.shadow.Operations.WILDCARD_GET_EXTENDS;
import static io.determann.shadow.api.shadow.Operations.WILDCARD_GET_SUPER;

public class WildcardSupport
{
   public static boolean equals(Wildcard wildcard, Object other)
   {
      return SupportSupport.equals(wildcard,
                                   Wildcard.class,
                                   other,
                                   WILDCARD_GET_EXTENDS,
                                   WILDCARD_GET_SUPER);
   }

   public static int hashCode(Wildcard wildcard)
   {
      return SupportSupport.hashCode(wildcard, WILDCARD_GET_EXTENDS, WILDCARD_GET_SUPER);
   }

   public static String toString(Wildcard wildcard)
   {
      return SupportSupport.toString(wildcard, Wildcard.class, WILDCARD_GET_EXTENDS, WILDCARD_GET_SUPER);
   }
}

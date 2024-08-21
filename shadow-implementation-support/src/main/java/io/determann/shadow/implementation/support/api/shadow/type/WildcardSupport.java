package io.determann.shadow.implementation.support.api.shadow.type;

import io.determann.shadow.api.shadow.type.C_Wildcard;
import io.determann.shadow.implementation.support.internal.shadow.SupportSupport;

import static io.determann.shadow.api.Operations.WILDCARD_GET_EXTENDS;
import static io.determann.shadow.api.Operations.WILDCARD_GET_SUPER;

public class WildcardSupport
{
   public static boolean equals(C_Wildcard wildcard, Object other)
   {
      return SupportSupport.equals(wildcard,
                                   C_Wildcard.class,
                                   other,
                                   WILDCARD_GET_EXTENDS,
                                   WILDCARD_GET_SUPER);
   }

   public static int hashCode(C_Wildcard wildcard)
   {
      return SupportSupport.hashCode(wildcard, WILDCARD_GET_EXTENDS, WILDCARD_GET_SUPER);
   }

   public static String toString(C_Wildcard wildcard)
   {
      return SupportSupport.toString(wildcard, C_Wildcard.class, WILDCARD_GET_EXTENDS, WILDCARD_GET_SUPER);
   }
}

package io.determann.shadow.implementation.support.api.shadow.directive;

import io.determann.shadow.api.C;
import io.determann.shadow.implementation.support.internal.shadow.SupportSupport;

import static io.determann.shadow.api.query.Operations.USES_GET_SERVICE;

public class UsesSupport
{
   public static boolean equals(C.Uses uses, Object other)
   {
      return SupportSupport.equals(uses, C.Uses.class, other, USES_GET_SERVICE);
   }

   public static int hashCode(C.Uses uses)
   {
      return SupportSupport.hashCode(uses, USES_GET_SERVICE);
   }

   public static String toString(C.Uses uses)
   {
      return SupportSupport.toString(uses, C.Uses.class, USES_GET_SERVICE);
   }
}

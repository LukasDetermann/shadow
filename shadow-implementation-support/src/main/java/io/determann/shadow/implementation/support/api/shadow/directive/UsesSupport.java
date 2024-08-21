package io.determann.shadow.implementation.support.api.shadow.directive;

import io.determann.shadow.api.shadow.directive.C_Uses;
import io.determann.shadow.implementation.support.internal.shadow.SupportSupport;

import static io.determann.shadow.api.Operations.USES_GET_SERVICE;

public class UsesSupport
{
   public static boolean equals(C_Uses uses, Object other)
   {
      return SupportSupport.equals(uses, C_Uses.class, other, USES_GET_SERVICE);
   }

   public static int hashCode(C_Uses uses)
   {
      return SupportSupport.hashCode(uses, USES_GET_SERVICE);
   }

   public static String toString(C_Uses uses)
   {
      return SupportSupport.toString(uses, C_Uses.class, USES_GET_SERVICE);
   }
}

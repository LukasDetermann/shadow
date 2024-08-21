package io.determann.shadow.implementation.support.api.shadow.directive;

import io.determann.shadow.api.shadow.directive.Uses;
import io.determann.shadow.implementation.support.internal.shadow.SupportSupport;

import static io.determann.shadow.api.Operations.USES_GET_SERVICE;

public class UsesSupport
{
   public static boolean equals(Uses uses, Object other)
   {
      return SupportSupport.equals(uses, Uses.class, other, USES_GET_SERVICE);
   }

   public static int hashCode(Uses uses)
   {
      return SupportSupport.hashCode(uses, USES_GET_SERVICE);
   }

   public static String toString(Uses uses)
   {
      return SupportSupport.toString(uses, Uses.class, USES_GET_SERVICE);
   }
}

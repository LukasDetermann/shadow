package io.determann.shadow.implementation.support.api.shadow.directive;

import io.determann.shadow.api.shadow.directive.Opens;
import io.determann.shadow.implementation.support.internal.shadow.SupportSupport;

import static io.determann.shadow.api.Operations.OPENS_GET_PACKAGE;
import static io.determann.shadow.api.Operations.OPENS_GET_TARGET_MODULES;

public class OpensSupport
{
   public static boolean equals(Opens opens, Object other)
   {
      return SupportSupport.equals(opens, Opens.class, other, OPENS_GET_TARGET_MODULES, OPENS_GET_PACKAGE);
   }

   public static int hashCode(Opens opens)
   {
      return SupportSupport.hashCode(opens, OPENS_GET_TARGET_MODULES, OPENS_GET_PACKAGE);
   }

   public static String toString(Opens opens)
   {
      return SupportSupport.toString(opens, Opens.class, OPENS_GET_PACKAGE, OPENS_GET_TARGET_MODULES);
   }
}

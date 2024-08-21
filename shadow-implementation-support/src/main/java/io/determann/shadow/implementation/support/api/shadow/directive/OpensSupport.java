package io.determann.shadow.implementation.support.api.shadow.directive;

import io.determann.shadow.api.shadow.directive.C_Opens;
import io.determann.shadow.implementation.support.internal.shadow.SupportSupport;

import static io.determann.shadow.api.Operations.OPENS_GET_PACKAGE;
import static io.determann.shadow.api.Operations.OPENS_GET_TARGET_MODULES;

public class OpensSupport
{
   public static boolean equals(C_Opens opens, Object other)
   {
      return SupportSupport.equals(opens, C_Opens.class, other, OPENS_GET_TARGET_MODULES, OPENS_GET_PACKAGE);
   }

   public static int hashCode(C_Opens opens)
   {
      return SupportSupport.hashCode(opens, OPENS_GET_TARGET_MODULES, OPENS_GET_PACKAGE);
   }

   public static String toString(C_Opens opens)
   {
      return SupportSupport.toString(opens, C_Opens.class, OPENS_GET_PACKAGE, OPENS_GET_TARGET_MODULES);
   }
}

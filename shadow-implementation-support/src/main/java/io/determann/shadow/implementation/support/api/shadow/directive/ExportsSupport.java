package io.determann.shadow.implementation.support.api.shadow.directive;

import io.determann.shadow.api.shadow.directive.C_Exports;
import io.determann.shadow.implementation.support.internal.shadow.SupportSupport;

import static io.determann.shadow.api.Operations.EXPORTS_GET_PACKAGE;
import static io.determann.shadow.api.Operations.EXPORTS_GET_TARGET_MODULES;

public class ExportsSupport
{
   public static boolean equals(C_Exports exports, Object other)
   {
      return SupportSupport.equals(exports, C_Exports.class, other, EXPORTS_GET_PACKAGE, EXPORTS_GET_TARGET_MODULES);
   }

   public static int hashCode(C_Exports exports)
   {
      return SupportSupport.hashCode(exports, EXPORTS_GET_PACKAGE, EXPORTS_GET_TARGET_MODULES);
   }

   public static String toString(C_Exports exports)
   {
      return SupportSupport.toString(exports, C_Exports.class, EXPORTS_GET_PACKAGE, EXPORTS_GET_TARGET_MODULES);
   }
}

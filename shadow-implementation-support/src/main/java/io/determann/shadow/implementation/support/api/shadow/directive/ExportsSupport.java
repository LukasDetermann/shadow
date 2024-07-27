package io.determann.shadow.implementation.support.api.shadow.directive;

import io.determann.shadow.api.shadow.directive.Exports;
import io.determann.shadow.implementation.support.internal.shadow.SupportSupport;

import static io.determann.shadow.api.shadow.Operations.EXPORTS_GET_PACKAGE;
import static io.determann.shadow.api.shadow.Operations.EXPORTS_GET_TARGET_MODULES;

public class ExportsSupport
{
   public static boolean equals(Exports exports, Object other)
   {
      return SupportSupport.equals(exports, Exports.class, other, EXPORTS_GET_PACKAGE, EXPORTS_GET_TARGET_MODULES);
   }

   public static int hashCode(Exports exports)
   {
      return SupportSupport.hashCode(exports, EXPORTS_GET_PACKAGE, EXPORTS_GET_TARGET_MODULES);
   }

   public static String toString(Exports exports)
   {
      return SupportSupport.toString(exports, Exports.class, EXPORTS_GET_PACKAGE, EXPORTS_GET_TARGET_MODULES);
   }
}

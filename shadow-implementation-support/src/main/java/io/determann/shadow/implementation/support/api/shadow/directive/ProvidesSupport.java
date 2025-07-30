package io.determann.shadow.implementation.support.api.shadow.directive;

import io.determann.shadow.api.C;
import io.determann.shadow.implementation.support.internal.shadow.SupportSupport;

import static io.determann.shadow.api.query.Operations.PROVIDES_GET_IMPLEMENTATIONS;
import static io.determann.shadow.api.query.Operations.PROVIDES_GET_SERVICE;

public class ProvidesSupport
{
   public static boolean equals(C.Provides provides, Object other)
   {
      return SupportSupport.equals(provides, C.Provides.class, other, PROVIDES_GET_IMPLEMENTATIONS, PROVIDES_GET_SERVICE);
   }

   public static int hashCode(C.Provides provides)
   {
      return SupportSupport.hashCode(provides, PROVIDES_GET_IMPLEMENTATIONS, PROVIDES_GET_SERVICE);
   }

   public static String toString(C.Provides provides)
   {
      return SupportSupport.toString(provides, C.Provides.class, PROVIDES_GET_SERVICE, PROVIDES_GET_IMPLEMENTATIONS);
   }
}

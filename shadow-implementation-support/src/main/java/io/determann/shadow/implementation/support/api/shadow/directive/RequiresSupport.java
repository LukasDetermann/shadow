package io.determann.shadow.implementation.support.api.shadow.directive;

import io.determann.shadow.api.C;
import io.determann.shadow.implementation.support.internal.shadow.SupportSupport;

import static io.determann.shadow.api.query.Operations.*;

public class RequiresSupport
{
   public static boolean equals(C.Requires requires, Object other)
   {
      return SupportSupport.equals(requires, C.Requires.class, other, REQUIRES_IS_STATIC, REQUIRES_IS_TRANSITIVE, REQUIRES_GET_DEPENDENCY);
   }

   public static int hashCode(C.Requires requires)
   {
      return SupportSupport.hashCode(requires, REQUIRES_IS_STATIC, REQUIRES_IS_TRANSITIVE, REQUIRES_GET_DEPENDENCY);
   }

   public static String toString(C.Requires requires)
   {
      return SupportSupport.toString(requires, C.Requires.class, REQUIRES_GET_DEPENDENCY, REQUIRES_IS_STATIC, REQUIRES_IS_TRANSITIVE);
   }
}

package io.determann.shadow.implementation.support.api.shadow.directive;

import io.determann.shadow.api.shadow.directive.Requires;
import io.determann.shadow.implementation.support.internal.shadow.SupportSupport;

import static io.determann.shadow.api.shadow.Operations.*;

public class RequiresSupport
{
   public static boolean equals(Requires requires, Object other)
   {
      return SupportSupport.equals(requires, Requires.class, other, REQUIRES_IS_STATIC, REQUIRES_IS_TRANSITIVE, REQUIRES_GET_DEPENDENCY);
   }

   public static int hashCode(Requires requires)
   {
      return SupportSupport.hashCode(requires, REQUIRES_IS_STATIC, REQUIRES_IS_TRANSITIVE, REQUIRES_GET_DEPENDENCY);
   }

   public static String toString(Requires requires)
   {
      return SupportSupport.toString(requires, Requires.class, REQUIRES_GET_DEPENDENCY, REQUIRES_IS_STATIC, REQUIRES_IS_TRANSITIVE);
   }
}

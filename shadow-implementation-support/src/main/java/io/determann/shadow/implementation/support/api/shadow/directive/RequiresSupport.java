package io.determann.shadow.implementation.support.api.shadow.directive;

import io.determann.shadow.api.shadow.directive.C_Requires;
import io.determann.shadow.implementation.support.internal.shadow.SupportSupport;

import static io.determann.shadow.api.Operations.*;

public class RequiresSupport
{
   public static boolean equals(C_Requires requires, Object other)
   {
      return SupportSupport.equals(requires, C_Requires.class, other, REQUIRES_IS_STATIC, REQUIRES_IS_TRANSITIVE, REQUIRES_GET_DEPENDENCY);
   }

   public static int hashCode(C_Requires requires)
   {
      return SupportSupport.hashCode(requires, REQUIRES_IS_STATIC, REQUIRES_IS_TRANSITIVE, REQUIRES_GET_DEPENDENCY);
   }

   public static String toString(C_Requires requires)
   {
      return SupportSupport.toString(requires, C_Requires.class, REQUIRES_GET_DEPENDENCY, REQUIRES_IS_STATIC, REQUIRES_IS_TRANSITIVE);
   }
}

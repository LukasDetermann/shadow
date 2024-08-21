package io.determann.shadow.implementation.support.api.shadow.type;

import io.determann.shadow.api.shadow.type.C_Intersection;
import io.determann.shadow.implementation.support.internal.shadow.SupportSupport;

import static io.determann.shadow.api.Operations.INTERSECTION_GET_BOUNDS;

public class IntersectionSupport
{
   public static boolean equals(C_Intersection intersection, Object other)
   {
      return SupportSupport.equals(intersection,
                                   C_Intersection.class,
                                   other,
                                   INTERSECTION_GET_BOUNDS);
   }

   public static int hashCode(C_Intersection intersection)
   {
      return SupportSupport.hashCode(intersection, INTERSECTION_GET_BOUNDS);
   }

   public static String toString(C_Intersection intersection)
   {
      return SupportSupport.toString(intersection, C_Intersection.class, INTERSECTION_GET_BOUNDS);
   }
}

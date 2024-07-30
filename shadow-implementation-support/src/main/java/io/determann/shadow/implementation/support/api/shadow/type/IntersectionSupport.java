package io.determann.shadow.implementation.support.api.shadow.type;

import io.determann.shadow.api.shadow.type.Intersection;
import io.determann.shadow.implementation.support.internal.shadow.SupportSupport;

import static io.determann.shadow.api.shadow.Operations.INTERSECTION_GET_BOUNDS;

public class IntersectionSupport
{
   public static boolean equals(Intersection intersection, Object other)
   {
      return SupportSupport.equals(intersection,
                                   Intersection.class,
                                   other,
                                   INTERSECTION_GET_BOUNDS);
   }

   public static int hashCode(Intersection intersection)
   {
      return SupportSupport.hashCode(intersection, INTERSECTION_GET_BOUNDS);
   }

   public static String toString(Intersection intersection)
   {
      return SupportSupport.toString(intersection, Intersection.class, INTERSECTION_GET_BOUNDS);
   }
}

package io.determann.shadow.implementation.support.api.shadow.type;

import io.determann.shadow.api.shadow.type.Array;
import io.determann.shadow.implementation.support.internal.shadow.SupportSupport;

import static io.determann.shadow.api.shadow.Operations.ARRAY_GET_COMPONENT_TYPE;

public class ArraySupport
{
   public static boolean equals(Array array, Object other)
   {
      return SupportSupport.equals(array,
                                   Array.class,
                                   other,
                                   ARRAY_GET_COMPONENT_TYPE);
   }

   public static int hashCode(Array array)
   {
      return SupportSupport.hashCode(array, ARRAY_GET_COMPONENT_TYPE);
   }

   public static String toString(Array array)
   {
      return SupportSupport.toString(array, Array.class, ARRAY_GET_COMPONENT_TYPE);
   }
}

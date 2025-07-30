package io.determann.shadow.implementation.support.api.shadow.type;

import io.determann.shadow.api.C;
import io.determann.shadow.implementation.support.internal.shadow.SupportSupport;

import static io.determann.shadow.api.query.Operations.ARRAY_GET_COMPONENT_TYPE;

public class ArraySupport
{
   public static boolean equals(C.Array array, Object other)
   {
      return SupportSupport.equals(array,
                                   C.Array.class,
                                   other,
                                   ARRAY_GET_COMPONENT_TYPE);
   }

   public static int hashCode(C.Array array)
   {
      return SupportSupport.hashCode(array, ARRAY_GET_COMPONENT_TYPE);
   }

   public static String toString(C.Array array)
   {
      return SupportSupport.toString(array, C.Array.class, ARRAY_GET_COMPONENT_TYPE);
   }

   public static boolean representsSameType(C.Array array, C.Type other)
   {
      return SupportSupport.representsSameType(array, C.Array.class, other, ARRAY_GET_COMPONENT_TYPE);
   }
}

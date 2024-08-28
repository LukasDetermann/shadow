package io.determann.shadow.implementation.support.api.shadow.type;

import io.determann.shadow.api.shadow.type.C_Array;
import io.determann.shadow.api.shadow.type.C_Type;
import io.determann.shadow.implementation.support.internal.shadow.SupportSupport;

import static io.determann.shadow.api.Operations.ARRAY_GET_COMPONENT_TYPE;

public class ArraySupport
{
   public static boolean equals(C_Array array, Object other)
   {
      return SupportSupport.equals(array,
                                   C_Array.class,
                                   other,
                                   ARRAY_GET_COMPONENT_TYPE);
   }

   public static int hashCode(C_Array array)
   {
      return SupportSupport.hashCode(array, ARRAY_GET_COMPONENT_TYPE);
   }

   public static String toString(C_Array array)
   {
      return SupportSupport.toString(array, C_Array.class, ARRAY_GET_COMPONENT_TYPE);
   }

   public static boolean representsSameType(C_Array array, C_Type other)
   {
      return SupportSupport.representsSameType(array, C_Array.class, other, ARRAY_GET_COMPONENT_TYPE);
   }
}

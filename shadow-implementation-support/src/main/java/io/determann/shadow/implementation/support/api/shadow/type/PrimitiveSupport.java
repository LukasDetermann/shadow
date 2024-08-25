package io.determann.shadow.implementation.support.api.shadow.type;

import io.determann.shadow.api.shadow.type.primitive.C_Primitive;
import io.determann.shadow.implementation.support.internal.shadow.SupportSupport;

public class PrimitiveSupport
{
   public static boolean equals(C_Primitive primitive, Object other)
   {
      return SupportSupport.equals(primitive,
                                   C_Primitive.class,
                                   other);
   }

   public static int hashCode(C_Primitive primitive)
   {
      return SupportSupport.hashCode(primitive);
   }

   public static String toString(C_Primitive primitive)
   {
      return SupportSupport.toString(primitive, C_Primitive.class);
   }
}

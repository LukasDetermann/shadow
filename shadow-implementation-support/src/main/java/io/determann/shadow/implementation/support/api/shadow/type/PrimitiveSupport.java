package io.determann.shadow.implementation.support.api.shadow.type;

import io.determann.shadow.api.C;
import io.determann.shadow.implementation.support.internal.shadow.SupportSupport;

public class PrimitiveSupport
{
   public static boolean equals(C.Primitive primitive, Object other)
   {
      return SupportSupport.equals(primitive,
                                   C.Primitive.class,
                                   other);
   }

   public static int hashCode(C.Primitive primitive)
   {
      return SupportSupport.hashCode(primitive);
   }

   public static String toString(C.Primitive primitive)
   {
      return SupportSupport.toString(primitive, C.Primitive.class);
   }
}

package io.determann.shadow.implementation.support.api.shadow.type;

import io.determann.shadow.api.shadow.type.C_Primitive;
import io.determann.shadow.implementation.support.internal.shadow.SupportSupport;

import static io.determann.shadow.api.Operations.SHADOW_GET_KIND;

public class PrimitiveSupport
{
   public static boolean equals(C_Primitive primitive, Object other)
   {
      return SupportSupport.equals(primitive,
                                   C_Primitive.class,
                                   other,
                                   SHADOW_GET_KIND);
   }

   public static int hashCode(C_Primitive primitive)
   {
      return SupportSupport.hashCode(primitive, SHADOW_GET_KIND);
   }

   public static String toString(C_Primitive primitive)
   {
      return SupportSupport.toString(primitive, C_Primitive.class, SHADOW_GET_KIND);
   }
}

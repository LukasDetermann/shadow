package io.determann.shadow.implementation.support.api.shadow.type;

import io.determann.shadow.api.shadow.type.Primitive;
import io.determann.shadow.implementation.support.internal.shadow.SupportSupport;

import static io.determann.shadow.api.shadow.Operations.SHADOW_GET_KIND;

public class PrimitiveSupport
{
   public static boolean equals(Primitive primitive, Object other)
   {
      return SupportSupport.equals(primitive,
                                   Primitive.class,
                                   other,
                                   SHADOW_GET_KIND);
   }

   public static int hashCode(Primitive primitive)
   {
      return SupportSupport.hashCode(primitive, SHADOW_GET_KIND);
   }

   public static String toString(Primitive primitive)
   {
      return SupportSupport.toString(primitive, Primitive.class, SHADOW_GET_KIND);
   }
}

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
      return (switch (primitive)
              {
                 case C.boolean_ aBoolean -> "boolean";
                 case C.byte_ aByte -> "byte";
                 case C.char_ aChar -> "char";
                 case C.double_ aDouble -> "double";
                 case C.float_ aFloat -> "float";
                 case C.int_ anInt -> "int";
                 case C.long_ aLong -> "long";
                 case C.short_ aShort -> "short";
                 default -> throw new IllegalArgumentException();
              });
   }
}

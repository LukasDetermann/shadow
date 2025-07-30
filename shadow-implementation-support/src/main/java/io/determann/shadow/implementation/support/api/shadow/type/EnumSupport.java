package io.determann.shadow.implementation.support.api.shadow.type;

import io.determann.shadow.api.C;
import io.determann.shadow.implementation.support.internal.shadow.SupportSupport;

import static io.determann.shadow.api.query.Operations.MODULE_ENCLOSED_GET_MODULE;
import static io.determann.shadow.api.query.Operations.QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME;

public class EnumSupport
{
   public static boolean equals(C.Enum anEnum, Object other)
   {
      return DeclaredSupport.equals(anEnum, other, C.Enum.class);
   }

   public static int hashCode(C.Enum anEnum)
   {
      return DeclaredSupport.hashCode(anEnum);
   }

   public static String toString(C.Enum anEnum)
   {
      return DeclaredSupport.toString(anEnum, C.Enum.class);
   }

   public static boolean representsSameType(C.Enum anEnum, C.Type other)
   {
      return SupportSupport.representsSameType(anEnum, C.Enum.class, other,
                                               MODULE_ENCLOSED_GET_MODULE,
                                               //should be the binary name. this is close enough for most cases
                                               QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME);
   }
}

package io.determann.shadow.implementation.support.api.shadow.type;

import io.determann.shadow.api.shadow.type.Enum;
import io.determann.shadow.api.shadow.type.Shadow;
import io.determann.shadow.implementation.support.internal.shadow.SupportSupport;

import static io.determann.shadow.api.shadow.Operations.MODULE_ENCLOSED_GET_MODULE;
import static io.determann.shadow.api.shadow.Operations.QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME;

public class EnumSupport
{
   public static boolean equals(Enum anEnum, Object other)
   {
      return DeclaredSupport.equals(anEnum, other, Enum.class);
   }

   public static int hashCode(Enum anEnum)
   {
      return DeclaredSupport.hashCode(anEnum);
   }

   public static String toString(Enum anEnum)
   {
      return DeclaredSupport.toString(anEnum, Enum.class);
   }

   public static boolean representsSameType(Enum anEnum, Shadow other)
   {
      return SupportSupport.representsSameType(anEnum, Enum.class, other,
                                               MODULE_ENCLOSED_GET_MODULE,
                                               //should be the binary name. this is close enough for most cases
                                               QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME);
   }
}

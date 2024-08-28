package io.determann.shadow.implementation.support.api.shadow.type;

import io.determann.shadow.api.shadow.type.C_Enum;
import io.determann.shadow.api.shadow.type.C_Type;
import io.determann.shadow.implementation.support.internal.shadow.SupportSupport;

import static io.determann.shadow.api.Operations.MODULE_ENCLOSED_GET_MODULE;
import static io.determann.shadow.api.Operations.QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME;

public class EnumSupport
{
   public static boolean equals(C_Enum anEnum, Object other)
   {
      return DeclaredSupport.equals(anEnum, other, C_Enum.class);
   }

   public static int hashCode(C_Enum anEnum)
   {
      return DeclaredSupport.hashCode(anEnum);
   }

   public static String toString(C_Enum anEnum)
   {
      return DeclaredSupport.toString(anEnum, C_Enum.class);
   }

   public static boolean representsSameType(C_Enum anEnum, C_Type other)
   {
      return SupportSupport.representsSameType(anEnum, C_Enum.class, other,
                                               MODULE_ENCLOSED_GET_MODULE,
                                               //should be the binary name. this is close enough for most cases
                                               QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME);
   }
}

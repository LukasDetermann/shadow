package io.determann.shadow.implementation.support.api.shadow.type;

import io.determann.shadow.api.shadow.type.Enum;

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
}

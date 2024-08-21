package io.determann.shadow.implementation.support.api.shadow.type;

import io.determann.shadow.api.shadow.type.C_Interface;

public class InterfaceSupport
{
   public static boolean equals(C_Interface anInterface, Object other)
   {
      return DeclaredSupport.equals(anInterface, other, C_Interface.class);
   }

   public static int hashCode(C_Interface anInterface)
   {
      return DeclaredSupport.hashCode(anInterface);
   }

   public static String toString(C_Interface anInterface)
   {
      return DeclaredSupport.toString(anInterface, C_Interface.class);
   }
}

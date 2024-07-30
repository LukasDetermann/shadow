package io.determann.shadow.implementation.support.api.shadow.type;

import io.determann.shadow.api.shadow.type.Interface;

public class InterfaceSupport
{
   public static boolean equals(Interface anInterface, Object other)
   {
      return DeclaredSupport.equals(anInterface, other, Interface.class);
   }

   public static int hashCode(Interface anInterface)
   {
      return DeclaredSupport.hashCode(anInterface);
   }

   public static String toString(Interface anInterface)
   {
      return DeclaredSupport.toString(anInterface, Interface.class);
   }
}

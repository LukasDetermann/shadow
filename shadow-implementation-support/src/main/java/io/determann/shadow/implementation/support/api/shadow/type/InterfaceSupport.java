package io.determann.shadow.implementation.support.api.shadow.type;

import io.determann.shadow.api.C;

public class InterfaceSupport
{
   public static boolean equals(C.Interface anInterface, Object other)
   {
      return DeclaredSupport.equals(anInterface, other, C.Interface.class);
   }

   public static int hashCode(C.Interface anInterface)
   {
      return DeclaredSupport.hashCode(anInterface);
   }

   public static String toString(C.Interface anInterface)
   {
      return DeclaredSupport.toString(anInterface, C.Interface.class);
   }
}

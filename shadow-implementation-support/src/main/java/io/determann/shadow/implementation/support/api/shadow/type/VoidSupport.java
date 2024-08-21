package io.determann.shadow.implementation.support.api.shadow.type;

import io.determann.shadow.api.shadow.type.C_Void;

import java.util.Objects;

public class VoidSupport
{
   public static boolean equals(C_Void aVoid, Object other)
   {
      return other instanceof C_Void;
   }

   public static int hashCode(C_Void aVoid)
   {
      return Objects.hash(C_Void.class);
   }

   public static String toString(C_Void aVoid)
   {
      return C_Void.class.getSimpleName();
   }
}

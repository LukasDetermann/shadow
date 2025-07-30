package io.determann.shadow.implementation.support.api.shadow.type;

import io.determann.shadow.api.C;

import java.util.Objects;

public class VoidSupport
{
   public static boolean equals(C.Void aVoid, Object other)
   {
      return other instanceof C.Void;
   }

   public static int hashCode(C.Void aVoid)
   {
      return Objects.hash(C.Void.class);
   }

   public static String toString(C.Void aVoid)
   {
      return C.Void.class.getSimpleName();
   }
}

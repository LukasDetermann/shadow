package io.determann.shadow.implementation.support.api.shadow.type;

import io.determann.shadow.api.shadow.type.Void;

import java.util.Objects;

public class VoidSupport
{
   public static boolean equals(Void aVoid, Object other)
   {
      return other instanceof Void;
   }

   public static int hashCode(Void aVoid)
   {
      return Objects.hash(Void.class);
   }

   public static String toString(Void aVoid)
   {
      return Void.class.getSimpleName();
   }
}

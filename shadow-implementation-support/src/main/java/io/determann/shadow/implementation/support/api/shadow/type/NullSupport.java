package io.determann.shadow.implementation.support.api.shadow.type;

import io.determann.shadow.api.shadow.type.Null;
import io.determann.shadow.api.shadow.type.Shadow;

import java.util.Objects;

public class NullSupport
{
   public static boolean equals(Null aNull, Object other)
   {
      return other instanceof Null;
   }

   public static int hashCode(Null aNull)
   {
      return Objects.hash(Null.class);
   }

   public static String toString(Null aNull)
   {
      return Null.class.getSimpleName();
   }

   public static boolean representsSameType(Null aNull, Shadow other)
   {
      return other instanceof Null;
   }
}

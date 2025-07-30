package io.determann.shadow.implementation.support.api.shadow.type;

import io.determann.shadow.api.C;

import java.util.Objects;

public class NullSupport
{
   public static boolean equals(C.Null aNull, Object other)
   {
      return other instanceof C.Null;
   }

   public static int hashCode(C.Null aNull)
   {
      return Objects.hash(C.Null.class);
   }

   public static String toString(C.Null aNull)
   {
      return C.Null.class.getSimpleName();
   }

   public static boolean representsSameType(C.Null aNull, C.Type other)
   {
      return other instanceof C.Null;
   }
}

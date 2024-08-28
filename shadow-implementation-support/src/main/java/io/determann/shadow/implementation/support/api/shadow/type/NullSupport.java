package io.determann.shadow.implementation.support.api.shadow.type;

import io.determann.shadow.api.shadow.type.C_Null;
import io.determann.shadow.api.shadow.type.C_Type;

import java.util.Objects;

public class NullSupport
{
   public static boolean equals(C_Null aNull, Object other)
   {
      return other instanceof C_Null;
   }

   public static int hashCode(C_Null aNull)
   {
      return Objects.hash(C_Null.class);
   }

   public static String toString(C_Null aNull)
   {
      return C_Null.class.getSimpleName();
   }

   public static boolean representsSameType(C_Null aNull, C_Type other)
   {
      return other instanceof C_Null;
   }
}

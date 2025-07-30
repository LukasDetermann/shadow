package io.determann.shadow.implementation.support.api.shadow.type;

import io.determann.shadow.api.C;

public class RecordSupport
{
   public static boolean equals(C.Record record, Object other)
   {
      return DeclaredSupport.equals(record, other, C.Record.class);
   }

   public static int hashCode(C.Record record)
   {
      return DeclaredSupport.hashCode(record);
   }

   public static String toString(C.Record record)
   {
      return DeclaredSupport.toString(record, C.Record.class);
   }
}

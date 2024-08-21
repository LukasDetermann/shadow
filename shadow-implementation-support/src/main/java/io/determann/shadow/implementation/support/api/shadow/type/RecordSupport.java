package io.determann.shadow.implementation.support.api.shadow.type;

import io.determann.shadow.api.shadow.type.C_Record;

public class RecordSupport
{
   public static boolean equals(C_Record record, Object other)
   {
      return DeclaredSupport.equals(record, other, C_Record.class);
   }

   public static int hashCode(C_Record record)
   {
      return DeclaredSupport.hashCode(record);
   }

   public static String toString(C_Record record)
   {
      return DeclaredSupport.toString(record, C_Record.class);
   }
}

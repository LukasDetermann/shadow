package io.determann.shadow.implementation.support.api.shadow.type;

import io.determann.shadow.api.shadow.type.Record;

public class RecordSupport
{
   public static boolean equals(Record record, Object other)
   {
      return DeclaredSupport.equals(record, other, Record.class);
   }

   public static int hashCode(Record record)
   {
      return DeclaredSupport.hashCode(record);
   }

   public static String toString(Record record)
   {
      return DeclaredSupport.toString(record, Record.class);
   }
}

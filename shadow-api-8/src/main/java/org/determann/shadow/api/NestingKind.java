package org.determann.shadow.api;

public enum NestingKind
{
   OUTER,
   INNER;

   public boolean isNested()
   {
      return this != OUTER;
   }
}

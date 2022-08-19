package org.determann.shadow.example.processed.test.method;

public abstract class SubSignature
{
   public abstract void first();

   public static class Inner extends SubSignature
   {
      @Override
      public void first()
      {

      }
   }
}

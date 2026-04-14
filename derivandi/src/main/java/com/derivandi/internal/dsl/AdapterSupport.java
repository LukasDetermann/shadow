package com.derivandi.internal.dsl;

public class AdapterSupport
{
   public static UnsupportedOperationException notImplemented()
   {
      throw new UnsupportedOperationException("not implemented");
   }
}
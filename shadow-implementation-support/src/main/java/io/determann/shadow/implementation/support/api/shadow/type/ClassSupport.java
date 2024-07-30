package io.determann.shadow.implementation.support.api.shadow.type;

import io.determann.shadow.api.shadow.type.Class;

public class ClassSupport
{
   public static boolean equals(Class aClass, Object other)
   {
      return DeclaredSupport.equals(aClass, other, Class.class);
   }

   public static int hashCode(Class aClass)
   {
      return DeclaredSupport.hashCode(aClass);
   }

   public static String toString(Class aClass)
   {
      return DeclaredSupport.toString(aClass, Class.class);
   }
}

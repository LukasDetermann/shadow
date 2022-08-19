package org.determann.shadow.example.processed.test.clazz;

public sealed class PermittedSubClassesExample permits PermittedSubClassesExample.Child
{
   public static final class Child extends PermittedSubClassesExample
   {

   }
}

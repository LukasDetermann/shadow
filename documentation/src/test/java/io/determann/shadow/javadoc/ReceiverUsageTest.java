package io.determann.shadow.javadoc;

import org.junit.jupiter.api.Assertions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

public class ReceiverUsageTest
{
   //@start region="ReceiverUsageTest.method"
   //needed to be able to annotate Receivers
   @Target(ElementType.TYPE_USE)//@link substring="@Target" target="java.lang.annotation.Target"
   @interface ReceiverAnnotation {}

   static class ReceiverExample {
      void foo(@ReceiverAnnotation ReceiverExample ReceiverExample.this) {}//@highlight substring="ReceiverExample.this"
   }

   static  {
      //foo does not need a parameter
      Assertions.assertDoesNotThrow(() -> new ReceiverExample().foo());//@highlight substring="foo"
   }
   //@end
}

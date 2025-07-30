package io.determann.shadow;

import io.determann.shadow.api.C;
import io.determann.shadow.api.reflection.Adapter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ReflectionTest
{
   @Test
   void gettingStarted()
   {
      //@formatter:off
 //tag::gettingStarted[]
 C.Class type = Adapter.generalize(this.getClass());
 //end::gettingStarted[]
 // @formatter:on
      Assertions.assertNotNull(type);
   }
}

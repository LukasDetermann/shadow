package io.determann.shadow;

import io.determann.shadow.api.reflection.R_Adapter;
import io.determann.shadow.api.shadow.type.C_Class;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ReflectionTest
{
   @Test
   void gettingStarted()
   {
      //@formatter:off
 //tag::gettingStarted[]
 C_Class type = R_Adapter.generalize(this.getClass());
 //end::gettingStarted[]
 // @formatter:on
      Assertions.assertNotNull(type);
   }
}

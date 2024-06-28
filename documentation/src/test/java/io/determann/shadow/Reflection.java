package io.determann.shadow;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.shadow.type.Class;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Reflection
{
   @Test
   void gettingStarted()
   {
      //@formatter:off
 //tag::gettingStarted[]
 Class shadow = ReflectionAdapter.generalize(this.getClass());
 //end::gettingStarted[]
 // @formatter:on
      Assertions.assertNotNull(shadow);
   }
}

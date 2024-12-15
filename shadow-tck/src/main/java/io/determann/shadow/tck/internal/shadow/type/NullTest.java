package io.determann.shadow.tck.internal.shadow.type;

import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Operations.GET_NULL;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.tck.internal.TckTest.test;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NullTest
{
   @Test
   void nonNull()
   {
      test(implementation -> assertTrue(requestOrEmpty(implementation, GET_NULL).isPresent()));
   }
}

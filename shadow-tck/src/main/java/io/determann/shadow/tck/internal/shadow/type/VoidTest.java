package io.determann.shadow.tck.internal.shadow.type;

import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.query.Operations.GET_VOID;
import static io.determann.shadow.api.query.Provider.requestOrEmpty;
import static io.determann.shadow.tck.internal.TckTest.test;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VoidTest
{
   @Test
   void nonNull()
   {
      test(implementation -> assertTrue(requestOrEmpty(implementation, GET_VOID).isPresent()));
   }
}


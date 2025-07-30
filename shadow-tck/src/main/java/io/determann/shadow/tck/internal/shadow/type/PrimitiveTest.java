package io.determann.shadow.tck.internal.shadow.type;

import io.determann.shadow.api.C;
import io.determann.shadow.api.query.Operations;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.query.Operations.GET_CLASS;
import static io.determann.shadow.api.query.Operations.GET_INT;
import static io.determann.shadow.api.query.Provider.requestOrThrow;
import static io.determann.shadow.tck.internal.TckTest.test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class PrimitiveTest
{
   @Test
   void testIsAssignableFrom()
   {
      test(implementation ->
           {
              C.Primitive primitiveInt = requestOrThrow(implementation, GET_INT);
              C.Class number = requestOrThrow(implementation, GET_CLASS, "java.lang.Number");
              C.Class string = requestOrThrow(implementation, GET_CLASS, "java.lang.String");

              assertTrue(requestOrThrow(primitiveInt, Operations.PRIMITIVE_IS_ASSIGNABLE_FROM, number));
              assertTrue(requestOrThrow(primitiveInt, Operations.PRIMITIVE_IS_ASSIGNABLE_FROM, primitiveInt));
              assertFalse(requestOrThrow(primitiveInt, Operations.PRIMITIVE_IS_ASSIGNABLE_FROM, string));
           });
   }
}

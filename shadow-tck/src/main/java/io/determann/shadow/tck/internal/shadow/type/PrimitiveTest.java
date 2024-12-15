package io.determann.shadow.tck.internal.shadow.type;

import io.determann.shadow.api.Operations;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.primitive.C_Primitive;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Operations.GET_CLASS;
import static io.determann.shadow.api.Operations.GET_INT;
import static io.determann.shadow.api.Provider.requestOrThrow;
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
              C_Primitive primitiveInt = requestOrThrow(implementation, GET_INT);
              C_Class number = requestOrThrow(implementation, GET_CLASS, "java.lang.Number");
              C_Class string = requestOrThrow(implementation, GET_CLASS, "java.lang.String");

              assertTrue(requestOrThrow(primitiveInt, Operations.PRIMITIVE_IS_ASSIGNABLE_FROM, number));
              assertTrue(requestOrThrow(primitiveInt, Operations.PRIMITIVE_IS_ASSIGNABLE_FROM, primitiveInt));
              assertFalse(requestOrThrow(primitiveInt, Operations.PRIMITIVE_IS_ASSIGNABLE_FROM, string));
           });
   }
}

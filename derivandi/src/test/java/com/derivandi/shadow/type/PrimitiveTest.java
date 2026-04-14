package com.derivandi.shadow.type;

import com.derivandi.api.Ap;
import org.junit.jupiter.api.Test;

import static com.derivandi.api.test.ProcessorTest.processorTest;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class PrimitiveTest
{
   @Test
   void testIsAssignableFrom()
   {
      processorTest().process(context ->
                              {
                                 Ap.Primitive primitiveInt = context.getConstants().getPrimitiveInt();

                                 Ap.Class number = context.getClassOrThrow("java.lang.Number");
                                 Ap.Class string = context.getClassOrThrow("java.lang.String");

                                 assertTrue(primitiveInt.isAssignableFrom(number));
                                 assertTrue(primitiveInt.isAssignableFrom(primitiveInt));
                                 assertFalse(primitiveInt.isAssignableFrom(string));
                              });
   }
}

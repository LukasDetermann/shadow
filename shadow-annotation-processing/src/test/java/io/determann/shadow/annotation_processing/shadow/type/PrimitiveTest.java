package io.determann.shadow.annotation_processing.shadow.type;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class PrimitiveTest
{
   @Test
   void testIsAssignableFrom()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Primitive primitiveInt = context.getConstants().getPrimitiveInt();
                               ;
                               Ap.Class number = context.getClassOrThrow("java.lang.Number");
                               Ap.Class string = context.getClassOrThrow("java.lang.String");

                               assertTrue(primitiveInt.isAssignableFrom(number));
                               assertTrue(primitiveInt.isAssignableFrom(primitiveInt));
                               assertFalse(primitiveInt.isAssignableFrom(string));
                            })
                   .compile();
   }
}

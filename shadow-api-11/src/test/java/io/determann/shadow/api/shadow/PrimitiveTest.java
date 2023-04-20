package io.determann.shadow.api.shadow;

import io.determann.shadow.api.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class PrimitiveTest extends ShadowTest<Primitive>
{
   PrimitiveTest()
   {
      super(shadowApi -> shadowApi.getConstants().getPrimitiveInt());
   }

   @Test
   void testIsSubtypeOf()
   {
      ProcessorTest.process(shadowApi ->
                              {
                                 assertTrue(shadowApi.getConstants().getPrimitiveInt().isAssignableFrom(shadowApi.getClassOrThrow("java.lang.Number")));
                                 assertTrue(shadowApi.getConstants().getPrimitiveInt().isAssignableFrom(shadowApi.getConstants().getPrimitiveInt()));
                                 assertFalse(shadowApi.getConstants().getPrimitiveInt().isAssignableFrom(shadowApi.getClassOrThrow("java.lang.String")));
                              })
                   .compile();
   }

   @Test
   void testIsAssignableFrom()
   {
      ProcessorTest.process(shadowApi ->
                              {
                                 assertTrue(shadowApi.getConstants().getPrimitiveInt().isAssignableFrom(shadowApi.getClassOrThrow("java.lang.Number")));
                                 assertTrue(shadowApi.getConstants().getPrimitiveInt().isAssignableFrom(shadowApi.getConstants().getPrimitiveInt()));
                                 assertFalse(shadowApi.getConstants().getPrimitiveInt().isAssignableFrom(shadowApi.getClassOrThrow("java.lang.String")));

                                 assertTrue(shadowApi.getConstants().getPrimitiveInt().isAssignableFrom(shadowApi.getConstants().getPrimitiveInt()));
                              })
                   .compile();
   }
}

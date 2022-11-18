package org.determann.shadow.api.shadow;

import org.determann.shadow.api.test.CompilationTest;
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
      CompilationTest.process(shadowApi ->
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
      CompilationTest.process(shadowApi ->
                              {
                                 assertTrue(shadowApi.getConstants().getPrimitiveInt().isAssignableFrom(shadowApi.getClassOrThrow("java.lang.Number")));
                                 assertTrue(shadowApi.getConstants().getPrimitiveInt().isAssignableFrom(shadowApi.getConstants().getPrimitiveInt()));
                                 assertFalse(shadowApi.getConstants().getPrimitiveInt().isAssignableFrom(shadowApi.getClassOrThrow("java.lang.String")));

                                 assertTrue(shadowApi.getConstants().getPrimitiveInt().isAssignableFrom(shadowApi.getConstants().getPrimitiveInt()));
                              })
                     .compile();
   }
}

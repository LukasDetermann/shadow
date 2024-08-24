package io.determann.shadow.consistency.shadow;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.shadow.type.primitive.C_Primitive;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class PrimitiveTest extends ShadowTest<C_Primitive>
{
   PrimitiveTest()
   {
      super(context -> context.getConstants().getPrimitiveInt());
   }

   @Test
   void testIsSubtypeOf()
   {
      ProcessorTest.process(context ->
                            {
                               assertTrue(context.getConstants().getPrimitiveInt()
                                                .isAssignableFrom(context.getClassOrThrow("java.lang.Number")));
                               assertTrue(context.getConstants().getPrimitiveInt()
                                                .isAssignableFrom(context.getConstants().getPrimitiveInt()));
                               assertFalse(context.getConstants().getPrimitiveInt()
                                                 .isAssignableFrom(context.getClassOrThrow("java.lang.String")));
                            })
                   .compile();
   }

   @Test
   void testIsAssignableFrom()
   {
      ProcessorTest.process(context ->
                            {
                               assertTrue(context.getConstants().getPrimitiveInt()
                                                .isAssignableFrom(context.getClassOrThrow("java.lang.Number")));
                               assertTrue(context.getConstants().getPrimitiveInt()
                                                .isAssignableFrom(context.getConstants().getPrimitiveInt()));
                               assertFalse(context.getConstants().getPrimitiveInt()
                                                 .isAssignableFrom(context.getClassOrThrow("java.lang.String")));

                               assertTrue(context.getConstants().getPrimitiveInt()
                                                .isAssignableFrom(context.getConstants().getPrimitiveInt()));
                            })
                   .compile();
   }
}

package io.determann.shadow.api.shadow;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.lang_model.query.LangModelQueries.query;
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
                               assertTrue(query(shadowApi.getConstants()
                                                         .getPrimitiveInt())
                                                .isAssignableFrom(shadowApi.getClassOrThrow("java.lang.Number")));
                               assertTrue(query(shadowApi.getConstants()
                                                         .getPrimitiveInt())
                                                .isAssignableFrom(shadowApi.getConstants().getPrimitiveInt()));
                               assertFalse(query(shadowApi.getConstants()
                                                          .getPrimitiveInt())
                                                 .isAssignableFrom(shadowApi.getClassOrThrow("java.lang.String")));
                            })
                   .compile();
   }

   @Test
   void testIsAssignableFrom()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               assertTrue(query(shadowApi.getConstants()
                                                         .getPrimitiveInt())
                                                .isAssignableFrom(shadowApi.getClassOrThrow("java.lang.Number")));
                               assertTrue(query(shadowApi.getConstants()
                                                         .getPrimitiveInt())
                                                .isAssignableFrom(shadowApi.getConstants().getPrimitiveInt()));
                               assertFalse(query(shadowApi.getConstants()
                                                          .getPrimitiveInt())
                                                 .isAssignableFrom(shadowApi.getClassOrThrow("java.lang.String")));

                               assertTrue(query(shadowApi.getConstants()
                                                         .getPrimitiveInt())
                                                .isAssignableFrom(shadowApi.getConstants().getPrimitiveInt()));
                            })
                   .compile();
   }
}

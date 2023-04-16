package io.determann.shadow.api.shadow;

import io.determann.shadow.api.test.CompilationTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecordComponentTest extends ShadowTest<RecordComponent>
{
   RecordComponentTest()
   {
      super(shadowApi -> shadowApi.getRecordOrThrow("RecordComponentExample").getRecordComponentOrThrow("id"));
   }

   @Test
   void testIsSubtype()
   {
      CompilationTest.process(shadowApi -> assertTrue(getShadowSupplier().apply(shadowApi).isSubtypeOf(shadowApi.getClassOrThrow("java.lang.Number"))))
                     .withCodeToCompile("RecordComponentExample.java", "public record RecordComponentExample(Long id){}")
                     .compile();
   }

   @Test
   void testIsAssignableFrom()
   {
      CompilationTest.process(shadowApi -> assertTrue(getShadowSupplier().apply(shadowApi)
                                                                         .isAssignableFrom(shadowApi.getConstants().getPrimitiveLong())))
                     .withCodeToCompile("RecordComponentExample.java", "public record RecordComponentExample(Long id){}")
                     .compile();
   }

   @Test
   void testGetRecord()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 Record recordExample = shadowApi.getRecordOrThrow("RecordComponentExample");
                                 assertEquals(recordExample, recordExample.getRecordComponentOrThrow("id").getRecord());
                              })
                     .withCodeToCompile("RecordComponentExample.java", "public record RecordComponentExample(Long id){}")
                     .compile();
   }

   @Test
   void testGetType()
   {
      CompilationTest.process(shadowApi -> assertEquals(shadowApi.getClassOrThrow("java.lang.Long"),
                                                        shadowApi.getRecordOrThrow("RecordComponentExample")
                                                                 .getRecordComponentOrThrow("id")
                                                                 .getType()))
                     .withCodeToCompile("RecordComponentExample.java", "public record RecordComponentExample(Long id){}")
                     .compile();
   }

   @Test
   void testGetGetter()
   {
      CompilationTest.process(shadowApi -> assertEquals("id()",
                                                        shadowApi.getRecordOrThrow("RecordComponentExample")
                                                                 .getRecordComponentOrThrow("id")
                                                                 .getGetter()
                                                                 .toString()))
                     .withCodeToCompile("RecordComponentExample.java", "public record RecordComponentExample(Long id){}")
                     .compile();
   }

   @Test
   void testGetPackage()
   {
      CompilationTest.process(shadowApi -> assertEquals(shadowApi.getPackagesOrThrow("io.determann.shadow.example.processed.test.recordcomponent")
                                                                 .get(0),
                                                        shadowApi.getRecordOrThrow(
                                                                       "io.determann.shadow.example.processed.test.recordcomponent.RecordComponentExample")
                                                                 .getRecordComponentOrThrow("id")
                                                                 .getPackage()))
                     .withCodeToCompile("RecordComponentExample.java", """
                           package io.determann.shadow.example.processed.test.recordcomponent;

                           public record RecordComponentExample(Long id) {}""")
                     .compile();
   }
}

package org.determann.shadow.api.shadow;

import org.determann.shadow.api.test.CompilationTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecordComponentTest extends ShadowTest<RecordComponent>
{
   RecordComponentTest()
   {
      super(shadowApi -> shadowApi.getRecord("RecordComponentExample").getRecordComponent("id"));
   }

   @Test
   void testIsSubtype()
   {
      CompilationTest.process(shadowApi -> assertTrue(getShadowSupplier().apply(shadowApi).isSubtypeOf(shadowApi.getClass("java.lang.Number"))))
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
                                 Record recordExample = shadowApi.getRecord("RecordComponentExample");
                                 assertEquals(recordExample, recordExample.getRecordComponent("id").getRecord());
                              })
                     .withCodeToCompile("RecordComponentExample.java", "public record RecordComponentExample(Long id){}")
                     .compile();
   }

   @Test
   void testGetType()
   {
      CompilationTest.process(shadowApi -> assertEquals(shadowApi.getClass("java.lang.Long"),
                                                        shadowApi.getRecord("RecordComponentExample")
                                                                 .getRecordComponent("id")
                                                                 .getType()))
                     .withCodeToCompile("RecordComponentExample.java", "public record RecordComponentExample(Long id){}")
                     .compile();
   }

   @Test
   void testGetGetter()
   {
      CompilationTest.process(shadowApi -> assertEquals("id()",
                                                        shadowApi.getRecord("RecordComponentExample")
                                                                 .getRecordComponent("id")
                                                                 .getGetter()
                                                                 .toString()))
                     .withCodeToCompile("RecordComponentExample.java", "public record RecordComponentExample(Long id){}")
                     .compile();
   }

   @Test
   void testGetPackage()
   {
      CompilationTest.process(shadowApi -> assertEquals(shadowApi.getPackages("org.determann.shadow.example.processed.test.recordcomponent")
                                                                 .get(0),
                                                        shadowApi.getRecord(
                                                                       "org.determann.shadow.example.processed.test.recordcomponent.RecordComponentExample")
                                                                 .getRecordComponent("id")
                                                                 .getPackage()))
                     .withCodeToCompile("RecordComponentExample.java", """
                           package org.determann.shadow.example.processed.test.recordcomponent;

                           public record RecordComponentExample(Long id) {}""")
                     .compile();
   }
}

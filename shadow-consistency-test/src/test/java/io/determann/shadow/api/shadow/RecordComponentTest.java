package io.determann.shadow.api.shadow;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.lang_model.LangModelQueries.query;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecordComponentTest extends ShadowTest<RecordComponent>
{
   RecordComponentTest()
   {
      super(shadowApi -> query(shadowApi.getRecordOrThrow("RecordComponentExample")).getRecordComponentOrThrow("id"));
   }

   @Test
   void testIsSubtype()
   {
      ProcessorTest.process(shadowApi -> assertTrue(getShadowSupplier().apply(shadowApi)
                                                                       .isSubtypeOf(shadowApi.getClassOrThrow("java.lang.Number"))))
                   .withCodeToCompile("RecordComponentExample.java", "public record RecordComponentExample(Long id){}")
                   .compile();
   }

   @Test
   void testIsAssignableFrom()
   {
      ProcessorTest.process(shadowApi -> assertTrue(getShadowSupplier().apply(shadowApi)
                                                                       .isAssignableFrom(shadowApi.getConstants().getPrimitiveLong())))
                   .withCodeToCompile("RecordComponentExample.java", "public record RecordComponentExample(Long id){}")
                   .compile();
   }

   @Test
   void testGetRecord()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               Record recordExample = shadowApi.getRecordOrThrow("RecordComponentExample");
                               assertEquals(recordExample, query(recordExample).getRecordComponentOrThrow("id").getRecord());
                            })
                   .withCodeToCompile("RecordComponentExample.java", "public record RecordComponentExample(Long id){}")
                   .compile();
   }

   @Test
   void testGetType()
   {
      ProcessorTest.process(shadowApi -> assertEquals(shadowApi.getClassOrThrow("java.lang.Long"),
                                                      query(shadowApi.getRecordOrThrow("RecordComponentExample"))
                                                               .getRecordComponentOrThrow("id")
                                                               .getType()))
                   .withCodeToCompile("RecordComponentExample.java", "public record RecordComponentExample(Long id){}")
                   .compile();
   }

   @Test
   void testGetGetter()
   {
      ProcessorTest.process(shadowApi -> assertEquals("id()",
                                                      query(shadowApi.getRecordOrThrow("RecordComponentExample"))
                                                               .getRecordComponentOrThrow("id")
                                                               .getGetter()
                                                               .toString()))
                   .withCodeToCompile("RecordComponentExample.java", "public record RecordComponentExample(Long id){}")
                   .compile();
   }

   @Test
   void testGetPackage()
   {
      ProcessorTest.process(shadowApi -> assertEquals(shadowApi.getPackages("io.determann.shadow.example.processed.test.recordcomponent")
                                                               .get(0),
                                                      query(shadowApi.getRecordOrThrow(
                                                                     "io.determann.shadow.example.processed.test.recordcomponent.RecordComponentExample"))
                                                               .getRecordComponentOrThrow("id")
                                                               .getPackage()))
                   .withCodeToCompile("RecordComponentExample.java", """
                         package io.determann.shadow.example.processed.test.recordcomponent;

                         public record RecordComponentExample(Long id) {}""")
                   .compile();
   }
}

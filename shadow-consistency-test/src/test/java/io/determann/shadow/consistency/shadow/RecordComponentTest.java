package io.determann.shadow.consistency.shadow;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.lang_model.shadow.type.LM_Record;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecordComponentTest
{
   @Test
   void testIsSubtype()
   {
      ProcessorTest.process(context -> assertTrue(context.getRecordOrThrow("RecordComponentExample").getRecordComponentOrThrow("id")
                                                                       .isSubtypeOf(context.getClassOrThrow("java.lang.Number"))))
                   .withCodeToCompile("RecordComponentExample.java", "public record RecordComponentExample(Long id){}")
                   .compile();
   }

   @Test
   void testIsAssignableFrom()
   {
      ProcessorTest.process(context -> assertTrue(context.getRecordOrThrow("RecordComponentExample").getRecordComponentOrThrow("id")
                                                                       .isAssignableFrom(context.getConstants().getPrimitiveLong())))
                   .withCodeToCompile("RecordComponentExample.java", "public record RecordComponentExample(Long id){}")
                   .compile();
   }

   @Test
   void testGetRecord()
   {
      ProcessorTest.process(context ->
                            {
                               LM_Record recordExample = context.getRecordOrThrow("RecordComponentExample");
                               assertEquals(recordExample, recordExample.getRecordComponentOrThrow("id").getRecord());
                            })
                   .withCodeToCompile("RecordComponentExample.java", "public record RecordComponentExample(Long id){}")
                   .compile();
   }

   @Test
   void testGetType()
   {
      ProcessorTest.process(context -> assertEquals(context.getClassOrThrow("java.lang.Long"),
                                                      context.getRecordOrThrow("RecordComponentExample")
                                                               .getRecordComponentOrThrow("id")
                                                               .getType()))
                   .withCodeToCompile("RecordComponentExample.java", "public record RecordComponentExample(Long id){}")
                   .compile();
   }

   @Test
   void testGetGetter()
   {
      ProcessorTest.process(context -> assertEquals("id()",
                                                      context.getRecordOrThrow("RecordComponentExample")
                                                               .getRecordComponentOrThrow("id")
                                                               .getGetter()
                                                               .toString()))
                   .withCodeToCompile("RecordComponentExample.java", "public record RecordComponentExample(Long id){}")
                   .compile();
   }

   @Test
   void testGetPackage()
   {
      ProcessorTest.process(context -> assertEquals(context.getPackages("io.determann.shadow.example.processed.test.recordcomponent")
                                                               .get(0),
                                                      context.getRecordOrThrow(
                                                                     "io.determann.shadow.example.processed.test.recordcomponent.RecordComponentExample")
                                                               .getRecordComponentOrThrow("id")
                                                               .getPackage()))
                   .withCodeToCompile("RecordComponentExample.java", """
                         package io.determann.shadow.example.processed.test.recordcomponent;

                         public record RecordComponentExample(Long id) {}""")
                   .compile();
   }
}

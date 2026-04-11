package io.determann.shadow.annotation_processing.shadow.structure;

import io.determann.shadow.api.annotation_processing.Ap;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.annotation_processing.test.ProcessorTest.processorTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecordComponentTest
{
   @Test
   void isSubtype()
   {
      processorTest().withCodeToCompile("RecordComponentExample.java", "public record RecordComponentExample(Long id){}")
                     .process(context ->
                              {
                                 Ap.Class number = context.getClassOrThrow("java.lang.Number");
                                 Ap.Record example = context.getRecordOrThrow("RecordComponentExample");
                                 Ap.RecordComponent id = example.getRecordComponentOrThrow("id");
                                 assertTrue(id.isSubtypeOf(number));
                              });
   }

   @Test
   void isAssignableFrom()
   {
      processorTest().withCodeToCompile("RecordComponentExample.java", "public record RecordComponentExample(Long id){}")
                     .process(context ->
                              {
                                 Ap.Record example = context.getRecordOrThrow("RecordComponentExample");
                                 Ap.RecordComponent id = example.getRecordComponentOrThrow("id");
                                 assertTrue(id.isAssignableFrom(context.getConstants().getPrimitiveLong()));
                              });
   }

   @Test
   void getRecord()
   {
      processorTest().withCodeToCompile("RecordComponentExample.java", "public record RecordComponentExample(Long id){}")
                     .process(context ->
                              {
                                 Ap.Record example = context.getRecordOrThrow("RecordComponentExample");
                                 Ap.RecordComponent id = example.getRecordComponentOrThrow("id");
                                 assertEquals(example, id.getRecord());
                              });
   }

   @Test
   void getType()
   {
      processorTest().withCodeToCompile("RecordComponentExample.java", "public record RecordComponentExample(Long id){}")
                     .process(context ->
                              {
                                 Ap.Class cLong = context.getClassOrThrow("java.lang.Long");

                                 Ap.Record example = context.getRecordOrThrow("RecordComponentExample");
                                 Ap.RecordComponent id = example.getRecordComponentOrThrow("id");
                                 assertEquals(cLong, id.getType());
                              });
   }
}

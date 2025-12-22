package io.determann.shadow.annotation_processing.shadow.structure;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecordComponentTest
{
   @Test
   void isSubtype()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class number = context.getClassOrThrow("java.lang.Number");
                               Ap.Record example = context.getRecordOrThrow("RecordComponentExample");
                               Ap.RecordComponent id = example.getRecordComponentOrThrow("id");
                               assertTrue(id.isSubtypeOf(number));
                            })
                   .withCodeToCompile("RecordComponentExample.java", "public record RecordComponentExample(Long id){}")
                   .compile();
   }

   @Test
   void isAssignableFrom()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Record example = context.getRecordOrThrow("RecordComponentExample");
                               Ap.RecordComponent id = example.getRecordComponentOrThrow("id");
                               assertTrue(id.isAssignableFrom(context.getConstants().getPrimitiveLong()));
                            })
                   .withCodeToCompile("RecordComponentExample.java", "public record RecordComponentExample(Long id){}")
                   .compile();
   }

   @Test
   void getRecord()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Record example = context.getRecordOrThrow("RecordComponentExample");
                               Ap.RecordComponent id = example.getRecordComponentOrThrow("id");
                               assertEquals(example, id.getRecord());
                            })
                   .withCodeToCompile("RecordComponentExample.java", "public record RecordComponentExample(Long id){}")
                   .compile();
   }

   @Test
   void getType()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class cLong = context.getClassOrThrow("java.lang.Long");

                               Ap.Record example = context.getRecordOrThrow("RecordComponentExample");
                               Ap.RecordComponent id = example.getRecordComponentOrThrow("id");
                               assertEquals(cLong, id.getType());
                            })
                   .withCodeToCompile("RecordComponentExample.java", "public record RecordComponentExample(Long id){}")
                   .compile();
   }
}

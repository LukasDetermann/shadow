package io.determann.shadow.tck.internal.shadow.structure;

import io.determann.shadow.api.C;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.query.Operations.*;
import static io.determann.shadow.api.query.Provider.requestOrThrow;
import static io.determann.shadow.tck.internal.TckTest.withSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecordComponentTest
{
   @Test
   void isSubtype()
   {
      withSource("RecordComponentExample.java", "public record RecordComponentExample(Long id){}")
            .test(implementation ->
                  {
                     C.Class number = requestOrThrow(implementation, GET_CLASS, "java.lang.Number");
                     C.Record example = requestOrThrow(implementation, GET_RECORD, "RecordComponentExample");
                     C.RecordComponent id = requestOrThrow(example, RECORD_GET_RECORD_COMPONENT, "id");
                     assertTrue(requestOrThrow(id, RECORD_COMPONENT_IS_SUBTYPE_OF, number));
                  });
   }

   @Test
   void isAssignableFrom()
   {
      withSource("RecordComponentExample.java", "public record RecordComponentExample(Long id){}")
            .test(implementation ->
                  {
                     C.Record example = requestOrThrow(implementation, GET_RECORD, "RecordComponentExample");
                     C.RecordComponent id = requestOrThrow(example, RECORD_GET_RECORD_COMPONENT, "id");
                     assertTrue(requestOrThrow(id, RECORD_COMPONENT_IS_ASSIGNABLE_FROM, requestOrThrow(implementation, GET_LONG)));
                  });
   }

   @Test
   void getRecord()
   {
      withSource("RecordComponentExample.java", "public record RecordComponentExample(Long id){}")
            .test(implementation ->
                  {
                     C.Record example = requestOrThrow(implementation, GET_RECORD, "RecordComponentExample");
                     C.RecordComponent id = requestOrThrow(example, RECORD_GET_RECORD_COMPONENT, "id");
                     assertEquals(example, requestOrThrow(id, RECORD_COMPONENT_GET_RECORD));
                  });
   }

   @Test
   void getType()
   {
      withSource("RecordComponentExample.java", "public record RecordComponentExample(Long id){}")
            .test(implementation ->
                  {
                     C.Class cLong = requestOrThrow(implementation, GET_CLASS, "java.lang.Long");

                     C.Record example = requestOrThrow(implementation, GET_RECORD, "RecordComponentExample");
                     C.RecordComponent id = requestOrThrow(example, RECORD_GET_RECORD_COMPONENT, "id");
                     assertEquals(cLong, requestOrThrow(id, RECORD_COMPONENT_GET_TYPE));
                  });
   }
}

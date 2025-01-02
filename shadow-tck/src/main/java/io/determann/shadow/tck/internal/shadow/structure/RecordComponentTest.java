package io.determann.shadow.tck.internal.shadow.structure;

import io.determann.shadow.api.shadow.structure.C_RecordComponent;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Record;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;
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
                     C_Class number = requestOrThrow(implementation, GET_CLASS, "java.lang.Number");
                     C_Record example = requestOrThrow(implementation, GET_RECORD, "RecordComponentExample");
                     C_RecordComponent id = requestOrThrow(example, RECORD_GET_RECORD_COMPONENT, "id");
                     assertTrue(requestOrThrow(id, RECORD_COMPONENT_IS_SUBTYPE_OF, number));
                  });
   }

   @Test
   void isAssignableFrom()
   {
      withSource("RecordComponentExample.java", "public record RecordComponentExample(Long id){}")
            .test(implementation ->
                  {
                     C_Record example = requestOrThrow(implementation, GET_RECORD, "RecordComponentExample");
                     C_RecordComponent id = requestOrThrow(example, RECORD_GET_RECORD_COMPONENT, "id");
                     assertTrue(requestOrThrow(id, RECORD_COMPONENT_IS_ASSIGNABLE_FROM, requestOrThrow(implementation, GET_LONG)));
                  });
   }

   @Test
   void getRecord()
   {
      withSource("RecordComponentExample.java", "public record RecordComponentExample(Long id){}")
            .test(implementation ->
                  {
                     C_Record example = requestOrThrow(implementation, GET_RECORD, "RecordComponentExample");
                     C_RecordComponent id = requestOrThrow(example, RECORD_GET_RECORD_COMPONENT, "id");
                     assertEquals(example, requestOrThrow(id, RECORD_COMPONENT_GET_RECORD));
                  });
   }

   @Test
   void getType()
   {
      withSource("RecordComponentExample.java", "public record RecordComponentExample(Long id){}")
            .test(implementation ->
                  {
                     C_Class cLong = requestOrThrow(implementation, GET_CLASS, "java.lang.Long");

                     C_Record example = requestOrThrow(implementation, GET_RECORD, "RecordComponentExample");
                     C_RecordComponent id = requestOrThrow(example, RECORD_GET_RECORD_COMPONENT, "id");
                     assertEquals(cLong, requestOrThrow(id, RECORD_COMPONENT_GET_TYPE));
                  });
   }
}

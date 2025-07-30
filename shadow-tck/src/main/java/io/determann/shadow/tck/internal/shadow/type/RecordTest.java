package io.determann.shadow.tck.internal.shadow.type;

import io.determann.shadow.api.C;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static io.determann.shadow.api.query.Operations.*;
import static io.determann.shadow.api.query.Provider.requestOrThrow;
import static io.determann.shadow.tck.internal.TckTest.withSource;
import static org.junit.jupiter.api.Assertions.*;

   class RecordTest
{
   @Test
   void getRecordComponent()
   {
      withSource("RecordExample.java", "public record RecordExample(Long id) implements java.io.Serializable{}")
            .test(implementation ->
                  {
                     C.Record example = requestOrThrow(implementation, GET_RECORD, "RecordExample");
                     C.Class cLong = requestOrThrow(implementation, GET_CLASS, "java.lang.Long");

                     C.RecordComponent id = requestOrThrow(example, RECORD_GET_RECORD_COMPONENT, "id");
                     assertEquals("id", requestOrThrow(id, NAMEABLE_GET_NAME));
                     assertEquals(cLong, requestOrThrow(id, RECORD_COMPONENT_GET_TYPE));

                     assertThrows(NoSuchElementException.class, () -> requestOrThrow(example, RECORD_GET_RECORD_COMPONENT, "asdf"));
                  });
   }

   @Test
   void getDirectInterfaces()
   {
      withSource("RecordExample.java", "public record RecordExample(Long id) implements java.io.Serializable{}")
            .test(implementation ->
                  {
                     C.Record example = requestOrThrow(implementation, GET_RECORD, "RecordExample");
                     C.Interface serializable = requestOrThrow(implementation, GET_INTERFACE, "java.io.Serializable");

                     assertEquals(List.of(serializable), requestOrThrow(example, DECLARED_GET_DIRECT_INTERFACES));
                  });
   }

   @Test
   void isSubtypeOf()
   {
      withSource("RecordExample.java", "public record RecordExample(Long id) implements java.io.Serializable{}")
            .test(implementation ->
                  {
                     C.Record example = requestOrThrow(implementation, GET_RECORD, "RecordExample");
                     C.Class cRecord = requestOrThrow(implementation, GET_CLASS, "java.lang.Record");
                     C.Class number = requestOrThrow(implementation, GET_CLASS, "java.lang.Number");

                     assertTrue(requestOrThrow(example, DECLARED_IS_SUBTYPE_OF, cRecord));
                     assertTrue(requestOrThrow(example, DECLARED_IS_SUBTYPE_OF, example));
                     assertFalse(requestOrThrow(example, DECLARED_IS_SUBTYPE_OF, number));
                  });
   }

   @Test
   void getDirectSuperTypes()
   {
      withSource("RecordNoParent.java", "record RecordNoParent() {}")
            .withSource("RecordMultiParent.java", """
                  record RecordMultiParent() implements java.util.function.Consumer<RecordMultiParent>, java.util.function.Supplier<RecordMultiParent> {
                        @Override
                        public void accept(RecordMultiParent recordMultiParent) {}
                  
                        @Override
                        public RecordMultiParent get() {return null;}
                     }""")
            .test(implementation ->
                  {
                     C.Class cRecord = requestOrThrow(implementation, GET_CLASS, "java.lang.Record");
                     C.Interface consumer = requestOrThrow(implementation, GET_INTERFACE, "java.util.function.Consumer");
                     C.Interface supplier = requestOrThrow(implementation, GET_INTERFACE, "java.util.function.Supplier");

                     C.Record noParent = requestOrThrow(implementation, GET_RECORD, "RecordNoParent");
                     assertEquals(List.of(cRecord), requestOrThrow(noParent, DECLARED_GET_DIRECT_SUPER_TYPES));

                     C.Record multiParent = requestOrThrow(implementation, GET_RECORD, "RecordMultiParent");
                     assertEquals(List.of(cRecord, consumer, supplier), requestOrThrow(multiParent, DECLARED_GET_DIRECT_SUPER_TYPES));
                  });
   }

   @Test
   void getSuperTypes()
   {
      withSource("RecordNoParent.java", "record RecordNoParent() {}")
            .withSource("RecordMultiParent.java", """
                  record RecordMultiParent() implements java.util.function.Consumer<RecordMultiParent>, java.util.function.Supplier<RecordMultiParent> {
                        @Override
                        public void accept(RecordMultiParent recordMultiParent) {}
                  
                        @Override
                        public RecordMultiParent get() {return null;}
                     }""")
            .test(implementation ->
                  {
                     C.Class object = requestOrThrow(implementation, GET_CLASS, "java.lang.Object");
                     C.Class cRecord = requestOrThrow(implementation, GET_CLASS, "java.lang.Record");
                     C.Interface consumer = requestOrThrow(implementation, GET_INTERFACE, "java.util.function.Consumer");
                     C.Interface supplier = requestOrThrow(implementation, GET_INTERFACE, "java.util.function.Supplier");

                     C.Record noParent = requestOrThrow(implementation, GET_RECORD, "RecordNoParent");

                     assertEquals(Set.of(object, cRecord), requestOrThrow(noParent, DECLARED_GET_SUPER_TYPES));

                     C.Record multiParent = requestOrThrow(implementation, GET_RECORD, "RecordMultiParent");
                     assertEquals(Set.of(object, cRecord, consumer, supplier), requestOrThrow(multiParent, DECLARED_GET_SUPER_TYPES));
                  });
   }
}

package io.determann.shadow.tck.internal.shadow.type;

import io.determann.shadow.api.C;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.determann.shadow.api.query.Operations.*;
import static io.determann.shadow.api.query.Provider.requestOrThrow;
import static io.determann.shadow.tck.internal.TckTest.withSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IntersectionTest
{
   @Test
   void testGetBounds()
   {
      withSource("IntersectionExample.java",
                 "public class IntersectionExample<T extends java.util.Collection & java.io.Serializable>{}")
            .test(implementation ->
                  {
                     C.Interface collection = requestOrThrow(implementation, GET_INTERFACE, "java.util.Collection");
                     C.Interface serializable = requestOrThrow(implementation, GET_INTERFACE, "java.io.Serializable");
                     List<C.Interface> expected = List.of(collection, serializable);

                     C.Class intersectionExample = requestOrThrow(implementation, GET_CLASS, "IntersectionExample");
                     C.Generic generic = requestOrThrow(intersectionExample, CLASS_GET_GENERIC_DECLARATIONS).get(0);

                     assertEquals(expected, requestOrThrow(generic, GENERIC_GET_BOUNDS));
                  });
   }
}

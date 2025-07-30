package io.determann.shadow.tck.internal.shadow.type;

import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Generic;
import io.determann.shadow.api.shadow.type.C_Interface;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;
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
                     C_Interface collection = requestOrThrow(implementation, GET_INTERFACE, "java.util.Collection");
                     C_Interface serializable = requestOrThrow(implementation, GET_INTERFACE, "java.io.Serializable");
                     List<C_Interface> expected = List.of(collection, serializable);

                     C_Class intersectionExample = requestOrThrow(implementation, GET_CLASS, "IntersectionExample");
                     C_Generic generic = requestOrThrow(intersectionExample, CLASS_GET_GENERICS).get(0);

                     assertEquals(expected, requestOrThrow(generic, GENERIC_GET_BOUNDS));
                  });
   }
}

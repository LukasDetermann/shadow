package io.determann.shadow.annotation_processing.shadow.type;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntersectionTest
{
   @Test
   void testGetBounds()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Interface collection = context.getInterfaceOrThrow( "java.util.Collection");
                               Ap.Interface serializable = context.getInterfaceOrThrow( "java.io.Serializable");
                               List<Ap.Interface> expected = List.of(collection, serializable);

                               Ap.Class intersectionExample = context.getClassOrThrow("IntersectionExample");
                               Ap.Generic generic = intersectionExample.getGenericDeclarations().get(0);

                               assertEquals(expected, generic.getBounds());
                            })
                   .withCodeToCompile("IntersectionExample.java",
                                      "public class IntersectionExample<T extends java.util.Collection & java.io.Serializable>{}")
                   .compile();
   }
}

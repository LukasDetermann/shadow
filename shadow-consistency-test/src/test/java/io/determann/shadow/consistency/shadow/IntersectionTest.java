package io.determann.shadow.consistency.shadow;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.shadow.type.Intersection;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.determann.shadow.api.lang_model.LangModelQueries.query;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IntersectionTest extends ShadowTest<Intersection>
{
   IntersectionTest()
   {
      super(shadowApi -> ((Intersection) query(query(shadowApi.getClassOrThrow("IntersectionExample"))
                                                     .getGenerics()
                                                     .get(0)).getExtends()));
   }

   @Test
   void testGetBounds()
   {
      ProcessorTest.process(shadowApi -> assertEquals(List.of(shadowApi.getInterfaceOrThrow("java.util.Collection"),
                                                              shadowApi.getInterfaceOrThrow("java.io.Serializable")),
                                                      query(getShadowSupplier().apply(shadowApi)).getBounds()))
                   .withCodeToCompile("IntersectionExample.java",
                                      "public class IntersectionExample<T extends java.util.Collection & java.io.Serializable>{\n}")
                   .compile();
   }
}

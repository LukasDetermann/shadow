package io.determann.shadow.api.shadow;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.determann.shadow.api.converter.Converter.convert;
import static io.determann.shadow.api.lang_model.LangModelQueries.query;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IntersectionTest extends ShadowTest<Intersection>
{
   IntersectionTest()
   {
      super(shadowApi -> convert(query(shadowApi.getClassOrThrow("IntersectionExample"))
                                                           .getGenerics()
                                                           .get(0)
                                                           .getExtends()).toIntersectionOrThrow());
   }

   @Test
   void testGetBounds()
   {
      ProcessorTest.process(shadowApi -> assertEquals(List.of(shadowApi.getInterfaceOrThrow("java.util.Collection"),
                                                              shadowApi.getInterfaceOrThrow("java.io.Serializable")),
                                                      getShadowSupplier().apply(shadowApi).getBounds()))
                   .withCodeToCompile("IntersectionExample.java",
                                      "public class IntersectionExample<T extends java.util.Collection & java.io.Serializable>{\n}")
                   .compile();
   }
}

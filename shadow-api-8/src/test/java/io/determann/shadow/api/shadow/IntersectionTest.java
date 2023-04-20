package io.determann.shadow.api.shadow;

import io.determann.shadow.api.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static io.determann.shadow.api.ShadowApi.convert;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IntersectionTest extends ShadowTest<Intersection>
{
   IntersectionTest()
   {
      super(shadowApi -> convert(shadowApi.getClassOrThrow("IntersectionExample").getFormalGenerics().get(0).getExtends()).toIntersectionOrThrow());
   }

   @Test
   void testGetBounds()
   {
      ProcessorTest.process(shadowApi -> assertEquals(Arrays.asList(shadowApi.getInterfaceOrThrow("java.util.Collection"),
                                                                    shadowApi.getInterfaceOrThrow("java.io.Serializable")),
                                                      getShadowSupplier().apply(shadowApi).getBounds()))
                   .withCodeToCompile("IntersectionExample.java", "public class IntersectionExample<T extends java.util.Collection & java.io.Serializable>{\n}")
                   .compile();
   }
}

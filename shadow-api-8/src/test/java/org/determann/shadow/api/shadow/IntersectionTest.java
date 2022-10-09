package org.determann.shadow.api.shadow;

import org.determann.shadow.api.test.CompilationTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntersectionTest extends ShadowTest<Intersection>
{
   IntersectionTest()
   {
      super(shadowApi -> shadowApi.convert(shadowApi.getClass("IntersectionExample").getFormalGenerics().get(0).getExtends()).toIntersection());
   }

   @Test
   void testGetBounds()
   {
      CompilationTest.process(shadowApi -> assertEquals(Arrays.asList(shadowApi.getInterface("java.util.Collection"),
                                                                  shadowApi.getInterface("java.io.Serializable")),
                                                        getShadowSupplier().apply(shadowApi).getBounds()))
                     .withCodeToCompile("IntersectionExample.java", "public class IntersectionExample<T extends java.util.Collection & java.io.Serializable>{\n}")
                     .compile();
   }
}
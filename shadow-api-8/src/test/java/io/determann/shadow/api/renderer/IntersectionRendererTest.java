package io.determann.shadow.api.renderer;

import io.determann.shadow.api.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.ShadowApi.convert;
import static io.determann.shadow.api.ShadowApi.render;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IntersectionRendererTest
{

   @Test
   void declaration()
   {
      ProcessorTest.process(shadowApi -> assertEquals("java.util.Collection & java.io.Serializable",
                                                      render(convert(shadowApi.getClassOrThrow("IntersectionExample")
                                                                              .getFormalGenerics()
                                                                              .get(0)
                                                                              .getExtends())
                                                                   .toIntersectionOrThrow())
                                                            .declaration()))
                   .withCodeToCompile("IntersectionExample.java",
                                      "public class IntersectionExample<T extends java.util.Collection & java.io.Serializable>{\n}")
                   .compile();
   }
}
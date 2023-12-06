package io.determann.shadow.api.renderer;

import io.determann.shadow.api.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.converter.Converter.convert;
import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IntersectionRendererTest
{

   @Test
   void declaration()
   {
      ProcessorTest.process(shadowApi -> assertEquals("java.util.Collection & java.io.Serializable",
                                                      render(DEFAULT, convert(shadowApi.getClassOrThrow("IntersectionExample")
                                                                              .getGenerics()
                                                                              .get(0)
                                                                              .getExtends())
                                                                   .toIntersectionOrThrow())
                                                            .declaration()))
                   .withCodeToCompile("IntersectionExample.java",
                                      "public class IntersectionExample<T extends java.util.Collection & java.io.Serializable>{\n}")
                   .compile();
   }
}
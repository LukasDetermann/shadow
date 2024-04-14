package io.determann.shadow.api.renderer;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.consistency.ConsistencyTest;
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
      ConsistencyTest.compileTime(context -> context.getClassOrThrow("IntersectionExample"))
                     .runtime(stringClassFunction -> ReflectionAdapter.generalize(stringClassFunction.apply("IntersectionExample")))
                     .withCode("IntersectionExample.java",
                               "public class IntersectionExample<T extends java.util.Collection & java.io.Serializable>{\n}")
                     .test(aClass -> assertEquals("java.util.Collection & java.io.Serializable",
                                                  render(DEFAULT, convert(aClass.getGenerics()
                                                                                .get(0)
                                                                                .getExtends())
                                                        .toIntersectionOrThrow())
                                                        .declaration()));
   }
}
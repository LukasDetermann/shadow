package io.determann.shadow.api.renderer;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.consistency.ConsistencyTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.converter.Converter.convert;
import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.meta_meta.Operations.CLASS_GET_GENERICS;
import static io.determann.shadow.meta_meta.Operations.GENERIC_GET_EXTENDS;
import static io.determann.shadow.meta_meta.Provider.requestOrThrow;
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
                                                  render(DEFAULT, convert(requestOrThrow(requestOrThrow(aClass, CLASS_GET_GENERICS)
                                                                                .get(0), GENERIC_GET_EXTENDS))
                                                        .toIntersectionOrThrow())
                                                        .declaration()));
   }
}
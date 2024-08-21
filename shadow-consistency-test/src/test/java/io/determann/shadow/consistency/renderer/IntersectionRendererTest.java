package io.determann.shadow.consistency.renderer;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.shadow.type.Class;
import io.determann.shadow.api.shadow.type.Intersection;
import io.determann.shadow.consistency.test.ConsistencyTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Operations.CLASS_GET_GENERICS;
import static io.determann.shadow.api.Operations.GENERIC_GET_EXTENDS;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IntersectionRendererTest
{
   @Test
   void declaration()
   {
      ConsistencyTest.<Class>compileTime(context -> context.getClassOrThrow("IntersectionExample"))
                     .runtime(stringClassFunction -> ReflectionAdapter.generalize(stringClassFunction.apply("IntersectionExample")))
                     .withCode("IntersectionExample.java",
                               "public class IntersectionExample<T extends java.util.Collection & java.io.Serializable>{\n}")
                     .test(aClass -> assertEquals("java.util.Collection & java.io.Serializable",
                                                  render(DEFAULT, ((Intersection) requestOrThrow(requestOrThrow(aClass, CLASS_GET_GENERICS)
                                                                                                       .get(0), GENERIC_GET_EXTENDS)))
                                                        .declaration()));
   }
}
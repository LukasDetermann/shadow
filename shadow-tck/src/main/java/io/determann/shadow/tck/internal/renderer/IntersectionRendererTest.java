package io.determann.shadow.tck.internal.renderer;

import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Generic;
import io.determann.shadow.api.shadow.type.C_Intersection;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Operations.CLASS_GET_GENERICS;
import static io.determann.shadow.api.Operations.GENERIC_GET_EXTENDS;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.tck.internal.RenderingTestBuilder.renderingTest;

class IntersectionRendererTest
{
   @Test
   void declaration()
   {
      renderingTest(C_Class.class)
            .withSource("IntersectionExample.java",
                        "public class IntersectionExample<T extends java.util.Collection & java.io.Serializable>{\n}")
            .withToRender("IntersectionExample")
            .withRender(cInterface ->
                        {
                           C_Generic generic = requestOrThrow(cInterface, CLASS_GET_GENERICS).get(0);
                           C_Intersection intersection = (C_Intersection) requestOrThrow(generic, GENERIC_GET_EXTENDS);
                           return render(DEFAULT, intersection).declaration();
                        })
            .withExpected("java.util.Collection & java.io.Serializable")
            .test();
   }
}
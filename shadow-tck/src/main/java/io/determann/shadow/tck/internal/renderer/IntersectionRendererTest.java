package io.determann.shadow.tck.internal.renderer;

import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Generic;
import io.determann.shadow.api.shadow.type.C_Intersection;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.tck.internal.TckTest.withSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IntersectionRendererTest
{
   @Test
   void declaration()
   {
      withSource("IntersectionExample.java",
                 "public class IntersectionExample<T extends java.util.Collection & java.io.Serializable>{\n}")
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "IntersectionExample");
                     C_Generic generic = requestOrThrow(cClass, CLASS_GET_GENERICS).get(0);
                     C_Intersection intersection = (C_Intersection) requestOrThrow(generic, GENERIC_GET_EXTENDS);
                     assertEquals("java.util.Collection & java.io.Serializable", render(DEFAULT, intersection).declaration());
                  });
   }
}
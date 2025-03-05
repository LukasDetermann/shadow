package io.determann.shadow.tck.internal.renderer;

import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.tck.internal.TckTest.test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PrimitiveRendererTest
{
   @Test
   void booleanType()
   {
      test(implementation -> assertEquals("boolean", render(requestOrThrow(implementation, GET_BOOLEAN)).type(DEFAULT)));
   }

   @Test
   void byteType()
   {
      test(implementation -> assertEquals("byte", render(requestOrThrow(implementation, GET_BYTE)).type(DEFAULT)));
   }

   @Test
   void shortType()
   {
      test(implementation -> assertEquals("short", render(requestOrThrow(implementation, GET_SHORT)).type(DEFAULT)));
   }

   @Test
   void intType()
   {
      test(implementation -> assertEquals("int", render(requestOrThrow(implementation, GET_INT)).type(DEFAULT)));
   }

   @Test
   void longType()
   {
      test(implementation -> assertEquals("long", render(requestOrThrow(implementation, GET_LONG)).type(DEFAULT)));
   }

   @Test
   void charType()
   {
      test(implementation -> assertEquals("char", render(requestOrThrow(implementation, GET_CHAR)).type(DEFAULT)));
   }

   @Test
   void floatType()
   {
      test(implementation -> assertEquals("float", render(requestOrThrow(implementation, GET_FLOAT)).type(DEFAULT)));
   }

   @Test
   void doubleType()
   {
      test(implementation -> assertEquals("double", render(requestOrThrow(implementation, GET_DOUBLE)).type(DEFAULT)));
   }
}